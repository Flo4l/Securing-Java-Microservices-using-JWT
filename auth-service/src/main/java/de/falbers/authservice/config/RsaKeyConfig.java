package de.falbers.authservice.config;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import static de.falbers.jwt.JwtUtils.isPEMFormat;
import static de.falbers.jwt.JwtUtils.parsePEMKey;

@Configuration
public class RsaKeyConfig {

    @Value("${security.jwt.private-key}")
    private Resource privateKeyResource;

    @Value("${security.jwt.public-key}")
    private Resource publicKeyResource;

    @Bean
    public RSAKey rsaKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Lade privaten Schlüssel
        PrivateKey privateKey;
        try (var privateKeyStream = privateKeyResource.getInputStream()) {
            byte[] keyBytes = privateKeyStream.readAllBytes();

            if (isPEMFormat(keyBytes)) {
                keyBytes = parsePEMKey(keyBytes);  // Convert PEM to DER
            }

            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        }

        // Lade öffentlichen Schlüssel
        PublicKey publicKey;
        try (var publicKeyStream = publicKeyResource.getInputStream()) {
            byte[] keyBytes = publicKeyStream.readAllBytes();

            if (isPEMFormat(keyBytes)) {
                keyBytes = parsePEMKey(keyBytes);
            }

            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        }

        return new RSAKey.Builder((RSAPublicKey) publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }
}
