package de.falbers.dataservice.data;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserDataClient userDataClient;

    @GetMapping("/age")
    @PreAuthorize("hasRole('USER')")
    public String getAge() {
        long ageYears = ChronoUnit.YEARS.between(
                userDataClient.getBirthday(),
                LocalDateTime.now());

        return String.format("Sie sind %s Jahre alt", ageYears);
    }
}
