package com.gopi.airbnb.dto.requests;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.annotation.EnumNaming;

public record UserSignUp(
        @NotBlank(message = "Name is Required")
        String name,

        String gender,
        @NotBlank(message = "Email is Required")
        @Email(message = "please provide the valid email address")
        String email,

        @NotBlank(message = "password is required")
        @Size(min = 8)
        String password
) {
}
