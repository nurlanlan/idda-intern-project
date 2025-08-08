package com.idda.project.auth_service.service;

import com.idda.project.auth_service.config.RabbitMQConfig;
import com.idda.project.auth_service.domain.dto.request.LoginRequest;
import com.idda.project.auth_service.domain.dto.request.RegisterRequest;
import com.idda.project.auth_service.domain.dto.response.LoginResponse;
import com.idda.project.auth_service.domain.entity.User;
import com.idda.project.auth_service.domain.event.UserRegisteredEvent;
import com.idda.project.auth_service.repository.UserRepository;
import com.idda.project.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REFRESH_TOKEN_KEY_PREFIX = "user:refreshToken:";


    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists with this email");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setFullName(request.getFullName());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        UserRegisteredEvent event = new UserRegisteredEvent(savedUser.getId(), savedUser.getFullName());

        log.info("Preparing to send UserRegisteredEvent for userId: {}", savedUser.getId());
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    event
            );
            log.info("UserRegisteredEvent sent successfully to RabbitMQ!");
        } catch (Exception e) {
            log.error("Failed to send UserRegisteredEvent to RabbitMQ!", e);
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        String accessToken = jwtUtil.generateToken(user, true);
        String refreshToken = jwtUtil.generateToken(user, false);

        String key = REFRESH_TOKEN_KEY_PREFIX + user.getId();
        redisTemplate.opsForValue().set(key, refreshToken, jwtUtil.getRefreshTokenExpirationMs(), TimeUnit.MILLISECONDS);


        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateRefreshToken(refreshToken)) { // JwtUtil-ə yeni bir metod əlavə edəcəyik
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid or expired");
        }

        Long userId = jwtUtil.extractUserIdFromToken(refreshToken); // JwtUtil-ə yeni bir metod əlavə edəcəyik
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found for this token"));

        String redisKey = REFRESH_TOKEN_KEY_PREFIX + userId;
        String storedToken = (String) redisTemplate.opsForValue().get(redisKey);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is not valid or has been revoked.");
        }

        String newAccessToken = jwtUtil.generateToken(user, true);

        return new LoginResponse(newAccessToken, refreshToken); // Sadə versiya
    }

    public void logout(Long userId) {
        String redisKey = REFRESH_TOKEN_KEY_PREFIX + userId;
        redisTemplate.delete(redisKey);
        log.info("User {} logged out, refresh token revoked.", userId);
    }
}

