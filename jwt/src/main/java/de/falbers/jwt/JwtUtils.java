package de.falbers.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    public static boolean isPEMFormat(byte[] keyBytes) {
        String keyContent = new String(keyBytes);
        return keyContent.contains("-----BEGIN");
    }

    public static byte[] parsePEMKey(byte[] pemKey) {
        String pemContent = new String(pemKey)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("[-]+[A-Z ]+[-]+", "")
                .replaceAll("\\s+", "");
        return Base64.getDecoder().decode(pemContent);
    }
}
