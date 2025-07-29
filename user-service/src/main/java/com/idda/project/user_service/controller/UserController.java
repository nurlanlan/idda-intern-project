package com.idda.project.user_service.controller;

import com.idda.project.user_service.domain.dto.request.UpdateUserInfoRequest;
import com.idda.project.user_service.domain.dto.response.UserResponse;
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

//    @PutMapping("/{id}/full-name")
//    public ResponseEntity<UserResponse> updateUserFullName(
//            @PathVariable Long id,
//            @RequestBody UpdateUserInfoRequest request
//    ) {
//        return ResponseEntity.ok(userService.updateUserFullName(id, request));
//    }
//
//    @PutMapping("/{id}/address")
//    public ResponseEntity<UserResponse> updateUserAddress(
//            @PathVariable Long id,
//            @RequestBody UpdateUserInfoRequest request
//    ) {
//        return ResponseEntity.ok(userService.updateUserAddress(id, request));
//    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<UserResponse> updateUserInfo(
            @PathVariable Long id,
            @RequestBody UpdateUserInfoRequest request
    ) {
        return ResponseEntity.ok(userService.updateInfo(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

}
