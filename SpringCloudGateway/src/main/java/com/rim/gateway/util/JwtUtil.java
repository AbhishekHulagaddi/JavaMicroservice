package com.rim.gateway.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token){
        try{
            extractClaims(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean isExpired(String token){
        Date exp = extractClaims(token).getExpiration();
        return exp.before(new Date());
    }

    public String getEmail(String token){
        return extractClaims(token).getSubject();
    }

    public String getRole(String token){
        return (String) extractClaims(token).get("role");
    }
}
