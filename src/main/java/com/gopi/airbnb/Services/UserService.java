package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.LoginRequest;
import com.gopi.airbnb.dto.requests.UserSignUp;
import com.gopi.airbnb.dto.response.LoginResponse;
import com.gopi.airbnb.dto.response.UserSignUpResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
     UserSignUpResponse signUp(UserSignUp request);
     LoginResponse login(LoginRequest loginRequest);
     boolean checkEmailPresent(String email);
}
