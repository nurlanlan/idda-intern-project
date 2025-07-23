package com.idda.project.user_service.service.impl;

import com.idda.project.user_service.dto.request.UserLoginRequest;
import com.idda.project.user_service.dto.request.UserRegisterRequest;
import com.idda.project.user_service.dto.response.UserResponse;
import com.idda.project.user_service.entity.User;
import com.idda.project.user_service.repository.UserRepository;
import com.idda.project.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            UserResponse response = new UserResponse();
            response.setValid(false);
            response.setMessage("Email already exists");
            return response;
        }
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setPassword(request.getPassword());
        user = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setValid(true);
        response.setMessage("User registered successfully");
        return response;
    }

    @Override
    public UserResponse loginUser(UserLoginRequest request) {
        UserResponse response = new UserResponse();
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(request.getPassword())) {
                response.setId(user.getId());
                response.setFullName(user.getFullName());
                response.setEmail(user.getEmail());
                response.setValid(true);
                response.setMessage("Login successful");
                return response;
            }
        }
        response.setValid(false);
        response.setMessage("Invalid email or password");
        return response;
    }
}


