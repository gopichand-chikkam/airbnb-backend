package com.gopi.airbnb.Services.implimentation;


import com.gopi.airbnb.Services.UserService;
import com.gopi.airbnb.dto.requests.LoginRequest;
import com.gopi.airbnb.dto.requests.UserSignUp;
import com.gopi.airbnb.dto.response.LoginResponse;
import com.gopi.airbnb.dto.response.UserSignUpResponse;
import com.gopi.airbnb.entitys.Guest;
import com.gopi.airbnb.entitys.User;
import com.gopi.airbnb.enums.Gender;
import com.gopi.airbnb.exceptions.InvalidCredentialsException;
import com.gopi.airbnb.exceptions.ResourceAlreadyExistsException;
import com.gopi.airbnb.repository.GuestRepo;
import com.gopi.airbnb.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
        private final UserRepo userRepo;
        private final GuestRepo guestRepo;

        private final PasswordEncoder passwordEncoder;


        @Override
    public UserSignUpResponse signUp(UserSignUp request) {
        if(checkEmailPresent(request.email())){
            throw new ResourceAlreadyExistsException("User with this email already exists");
        }
        String hashedPassword= passwordEncoder.encode(request.password());
        User user= new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(hashedPassword);
        User savedUser= userRepo.save(user);
        return new UserSignUpResponse(savedUser.getId(),"User Registered Successful");

    }

    @Override
    public boolean checkEmailPresent(String email){
        return userRepo.existsByEmail(email);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
           if(!checkEmailPresent(loginRequest.email())){
               throw new UsernameNotFoundException("Email is not Registered");
           }
           User user = userRepo.findByEmail(loginRequest.email());

           boolean passwordMatch= passwordEncoder.matches(loginRequest.password(),user.getPassword());
        if(!passwordMatch){
            throw new InvalidCredentialsException("Invalid Credentials");
        }
        LoginResponse loginResponse= new LoginResponse(user.getId(),"Login Successfully ");
        return loginResponse;
    }

}
