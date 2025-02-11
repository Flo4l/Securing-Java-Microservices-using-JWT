package de.falbers.jwt;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(Feign.class)
public class JwtFeignInterceptor implements RequestInterceptor, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(JwtFeignInterceptor.class);

    private final JwtClient jwtClient;
    private final JwtDecoder jwtDecoder;

    @Value("${service.name}")
    private String serviceName;
    @Value("${service.secret}")
    private String serviceSecret;

    private String currentToken;


    @Autowired
    public JwtFeignInterceptor(JwtClient jwtClient,
                               JwtDecoder jwtDecoder) {
        this.jwtClient = jwtClient;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void afterPropertiesSet() {
        // Initialen Token laden
        TokenRequest tokenRequest = new TokenRequest(serviceName, serviceSecret);
        currentToken = jwtClient.requestToken(tokenRequest).token();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Token neu laden, falls abgelaufen
        if (isTokenExpired(currentToken)) {
            TokenRequest tokenRequest = new TokenRequest(serviceName, serviceSecret);
            currentToken = jwtClient.requestToken(tokenRequest).token();
            LOG.info("Token wurde erneuert.");
        }
        // Token dem Request anfügen
        requestTemplate.header("Authorization", "Bearer " + currentToken);
    }

    // Prüfen, ob der Gültigkeitszeitraum des Tokens überschritten ist
    private boolean isTokenExpired(String token) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(token);
            if (decodedJwt.getExpiresAt() == null ||
                    decodedJwt.getExpiresAt().isBefore(java.time.Instant.now())) {
                LOG.warn("Token ist abgelaufen.");
                return true;
            }
            return false;
        } catch (JwtValidationException e) {
            LOG.error("Token ist ungültig.");
            return true;
        }
    }
}
