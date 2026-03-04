package com.rim.gateway.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final Set<String> blockedTokens = ConcurrentHashMap.newKeySet();

    public void block(String token){
        blockedTokens.add(token);
    }

    public boolean isBlocked(String token){
        return blockedTokens.contains(token);
    }
}
