package com.idda.project.user_service.controller;

import com.idda.project.user_service.dto.request.UpdateUserInfoRequest;
import com.idda.project.user_service.dto.response.UserResponse;
import com.idda.project.user_service.service.UserService;
import com.idda.project.user_service.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserInfo(
            @PathVariable Long id,
            @RequestBody UpdateUserInfoRequest request
    ) {
        return ResponseEntity.ok(userService.updateUserInfo(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

}
