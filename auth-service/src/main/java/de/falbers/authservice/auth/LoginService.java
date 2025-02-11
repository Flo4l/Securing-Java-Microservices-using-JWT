package de.falbers.authservice.auth;

import com.nimbusds.jose.JOSEException;
import de.falbers.jwt.TokenRequest;
import de.falbers.jwt.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtGenerator jwtGenerator;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse authenticate(TokenRequest request) throws JOSEException {

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.login());

        // Vergleiche das Passwort
        if (passwordEncoder.matches(request.secret(), userDetails.getPassword())) {
            // Password stimmt überein, gib einen Token mit den Rollen des Users zurück
            List<String> roles = toStringRoles(userDetails.getAuthorities());
            String token = jwtGenerator.generateToken(userDetails.getUsername(), roles);
            return new TokenResponse(token);
        }
        // Passwort ungültig
        throw new BadCredentialsException("Ungültige Zugangsdaten");
    }

    private List<String> toStringRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).toList();
    }
}
