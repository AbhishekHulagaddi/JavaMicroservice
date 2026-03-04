package com.rim.gateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rim.gateway.util.TokenBlacklistService;

@RestController
@RequestMapping("/access")
public class TokenBlockController {

    @Autowired
    private TokenBlacklistService blacklistService;

    @PostMapping("/logout")
    public void block(@RequestBody Map<String,String> body){
        blacklistService.block(body.get("token"));
    }
}
