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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RabbitTemplate rabbitTemplate;

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

        return new LoginResponse(accessToken, refreshToken);
    }
}

