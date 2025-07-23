package com.idda.project.user_service.controller;

import com.idda.project.user_service.dto.request.UserLoginRequest;
import com.idda.project.user_service.dto.request.UserRegisterRequest;
import com.idda.project.user_service.dto.response.UserResponse;
import com.idda.project.user_service.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register-check")
    public ResponseEntity<UserResponse> checkRegister(@RequestBody UserRegisterRequest request){
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login-check")
    public ResponseEntity<UserResponse> checkLogin(@RequestBody UserLoginRequest request){
        UserResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}
