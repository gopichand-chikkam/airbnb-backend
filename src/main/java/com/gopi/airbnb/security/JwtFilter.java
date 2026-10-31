package com.gopi.airbnb.security;

import com.gopi.airbnb.Services.JwtService;
import com.gopi.airbnb.exceptions.InvalidDetailsException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
      private final JwtService jwtService;
      private final CustomUserDetailsImpl customUserDetails;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("entered doFilterInternal");
                  String authHeader=request.getHeader("Authorization");
                  String token=null;
                  String userName= null;
                  if(authHeader!=null && authHeader.startsWith("Bearer")){
                      System.out.println("entered 1st Condition");
                      token=authHeader.substring(7);
                      userName= jwtService.extractUserNameFromToken(token);
                      if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                          System.out.println("entered 2nd Condition");
                          UserDetails userDetails= customUserDetails.loadUserByUsername(userName);
                          if(jwtService.validateUser(userDetails,token)){
                              UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                              SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                          }else{
                              throw new InvalidDetailsException("Session token docent matched");
                          }
                      }
                  }
                      filterChain.doFilter(request,response);


    }
}
