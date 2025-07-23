package com.idda.project.auth_service.service;

import com.idda.project.auth_service.dto.request.LoginRequest;
import com.idda.project.auth_service.dto.request.RegisterRequest;
import com.idda.project.auth_service.dto.response.LoginResponse;
import com.idda.project.auth_service.entity.User;
import com.idda.project.auth_service.repository.UserRepository;
import com.idda.project.auth_service.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

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

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), true);
        String refreshToken = jwtUtil.generateToken(user.getEmail(), false);

        return new LoginResponse(accessToken, refreshToken);
    }
}

