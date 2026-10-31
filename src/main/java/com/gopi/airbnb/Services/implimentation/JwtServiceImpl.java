package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.JwtService;
import com.gopi.airbnb.dto.requests.LoginRequest;
import com.gopi.airbnb.exceptions.InvalidDetailsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtServiceImpl implements JwtService {

    static private final String secretKey = "gopiChand_Chikkam123AdityaSchoolKakinada1234567890";

    public SecretKey getKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public String generateToken(LoginRequest loginRequest) {
        HashMap<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(loginRequest.email())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000000 * 6 * 3))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUserNameFromToken(String token) {

        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
        System.out.println("Came to excatract claims");
        try {
            return Jwts.parser().verifyWith(getKey(secretKey)).build().parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            throw new InvalidDetailsException(e.getMessage());
        }

    }

    @Override
    public boolean validateUser(UserDetails userDetails, String token) {
         String userName= extractUserNameFromToken(token);
         return !extractClaims(token).getExpiration().before(new Date(System.currentTimeMillis())) && userName.equals(userDetails.getUsername());
    }
}
