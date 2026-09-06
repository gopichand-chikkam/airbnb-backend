package com.gopi.airbnb.controllers;


import com.gopi.airbnb.Services.UserService;
import com.gopi.airbnb.dto.requests.LoginRequest;
import com.gopi.airbnb.dto.requests.UserSignUp;
import com.gopi.airbnb.dto.response.LoginResponse;
import com.gopi.airbnb.dto.response.UserSignUpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class UserController {

    final private UserService userService;

    @PostMapping("/signUp")
    ResponseEntity<UserSignUpResponse> signUp(@Valid @RequestBody UserSignUp request) {
        UserSignUpResponse userSignUpResponse = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSignUpResponse);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);

        return ResponseEntity.ok(loginResponse);
    }


}
