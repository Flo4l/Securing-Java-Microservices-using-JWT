package de.falbers.authservice.data;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/birthday")
    @PreAuthorize("hasRole('DATA_SERVICE')")
    public Object getBirthday() {
        return LocalDate.now().minusYears(20);
    }
}
