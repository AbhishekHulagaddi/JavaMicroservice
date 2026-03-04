package com.rim.auth.utils;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rim.auth.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.exp}")
    private long accessExp;

    @Value("${jwt.refresh.exp}")
    private long refreshExp;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // =====================================================
    // ACCESS TOKEN
    // =====================================================
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().getRoleName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExp))
                .signWith(getKey())
                .compact();
    }

    // =====================================================
    // REFRESH TOKEN
    // =====================================================
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExp))
                .signWith(getKey())
                .compact();
    }

    // =====================================================
    // EXTRACT EMAIL
    // =====================================================
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // =====================================================
    // EXTRACT ROLE
    // =====================================================
    public String extractRole(String token) {
        return (String) extractClaims(token).get("role");
    }

    // =====================================================
    // VALIDATE TOKEN SIGNATURE
    // =====================================================
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // =====================================================
    // CHECK EXPIRY
    // =====================================================
    public boolean isTokenExpired(String token) {
        Date exp = extractClaims(token).getExpiration();
        return exp.before(new Date());
    }

    // =====================================================
    // EXTRACT CLAIMS
    // =====================================================
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    // =====================================================
    // TOKEN TYPE CHECK
    // =====================================================
    public boolean isRefreshToken(String token){
        return "refresh".equals(extractClaims(token).get("type"));
    }
}
