package de.falbers.dataservice.data;


import de.falbers.jwt.JwtFeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@FeignClient(name = "user-data-client", url = "${service.auth.url}", configuration = JwtFeignInterceptor.class)
public interface UserDataClient {

    @GetMapping("/api/user/birthday")
    LocalDate getBirthday();
}
