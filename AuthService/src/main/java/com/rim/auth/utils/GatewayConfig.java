package com.rim.auth.utils;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "gateway-client",
        url = "${gateway.base-url}"
)
public interface GatewayConfig {

    @PostMapping("/access/logout")
    void blockToken(@RequestBody Map<String,String> body);
}
