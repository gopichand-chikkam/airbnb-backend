package com.gopi.airbnb.dto.requests;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Email is Required")
        @Email(message = "please provide the valid email address")
        String email,

        @NotBlank(message = "password is required")
        String password) {
}

