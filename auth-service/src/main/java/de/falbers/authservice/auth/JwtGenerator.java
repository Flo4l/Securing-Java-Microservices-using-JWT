package de.falbers.authservice.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtGenerator {

    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000; // 1 Stunde

    private final RSAKey rsaKey;

    @Value("${service.auth.url}")
    private String authServiceUrl;

    public String generateToken(String login, List<String> roles) throws JOSEException {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(authServiceUrl)
                .subject(login)
                .claim("roles", roles)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
                claims
        );

        signedJWT.sign(new RSASSASigner(rsaKey.toPrivateKey()));
        return signedJWT.serialize();
    }
}
