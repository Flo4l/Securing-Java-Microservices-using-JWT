package de.falbers.jwt;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "jwtAuthServiceClient", url = "${service.auth.url}")
public interface JwtClient {

    @PostMapping(path = "/api/token")
    TokenResponse requestToken(@RequestBody TokenRequest request);
}