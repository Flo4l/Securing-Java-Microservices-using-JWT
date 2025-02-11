package de.falbers.authservice.auth;

import com.nimbusds.jose.JOSEException;
import de.falbers.jwt.TokenRequest;
import de.falbers.jwt.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/token")
    public TokenResponse authenticate(@RequestBody TokenRequest request) throws JOSEException {
        return loginService.authenticate(request);
    }
}
