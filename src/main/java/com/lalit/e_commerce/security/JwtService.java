package com.lalit.e_commerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long jwtExpirationMs;


    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration:3600000}") long jwtExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(
                                 secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(String email,String role){

        return Jwts.builder()
                .subject(email)
                .claim("role",role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token){

        try{
            return getClaims(token).getSubject();

        }catch (JwtException ex){
            throw new IllegalArgumentException("Invalid Jwt token: "+ex.getMessage());
        }
    }

    public String extractRole(String token){
        try{
            return getClaims(token).get("role",String.class);
        }catch(JwtException ex){
            throw new IllegalArgumentException("Invalid Jwt token: "+ex.getMessage());
        }
    }

    public boolean isTokenValid(String token){

        try{
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }


    private Claims getClaims(String token){

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
