package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.LoginRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(LoginRequest loginRequest);

    String extractUserNameFromToken(String token);

    boolean validateUser(UserDetails userDetails, String token);
}
