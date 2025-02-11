package de.falbers.dataservice.data;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/data")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminData() {
        return "Sensible Admin-Daten";
    }
}
