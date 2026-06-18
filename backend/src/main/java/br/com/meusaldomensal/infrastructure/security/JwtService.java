package br.com.meusaldomensal.infrastructure.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;
    private final byte[] secret;
    private final long expirationMinutes;

    public JwtService(
            ObjectMapper objectMapper,
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.expiration-minutes}") long expirationMinutes) {
        this.objectMapper = objectMapper;
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(AuthenticatedUser user) {
        try {
            Instant now = Instant.now();
            Map<String, Object> header = new LinkedHashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            Map<String, Object> claims = new LinkedHashMap<>();
            claims.put("sub", user.userId().toString());
            claims.put("name", user.name());
            claims.put("email", user.email());
            claims.put("tenantId", user.tenantId().toString());
            claims.put("accountName", user.accountName());
            claims.put("schemaName", user.schemaName());
            claims.put("role", user.role());
            claims.put("iat", now.getEpochSecond());
            claims.put("exp", now.plusSeconds(expirationMinutes * 60).getEpochSecond());

            String encodedHeader = encode(objectMapper.writeValueAsBytes(header));
            String encodedPayload = encode(objectMapper.writeValueAsBytes(claims));
            String unsignedToken = encodedHeader + "." + encodedPayload;
            return unsignedToken + "." + sign(unsignedToken);
        } catch (Exception exception) {
            throw new JwtValidationException("Nao foi possivel gerar o token.");
        }
    }

    public AuthenticatedUser parseToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new JwtValidationException("Token invalido.");
            }

            String unsignedToken = parts[0] + "." + parts[1];
            if (!MessageDigest.isEqual(sign(unsignedToken).getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
                throw new JwtValidationException("Token invalido.");
            }

            Map<String, Object> claims = objectMapper.readValue(Base64.getUrlDecoder().decode(parts[1]), MAP_TYPE);
            long expiresAt = numberClaim(claims, "exp");
            if (Instant.now().getEpochSecond() >= expiresAt) {
                throw new JwtValidationException("Token expirado.");
            }

            return new AuthenticatedUser(
                    UUID.fromString(stringClaim(claims, "sub")),
                    stringClaim(claims, "name"),
                    stringClaim(claims, "email"),
                    UUID.fromString(stringClaim(claims, "tenantId")),
                    stringClaim(claims, "accountName"),
                    stringClaim(claims, "schemaName"),
                    stringClaim(claims, "role"));
        } catch (JwtValidationException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new JwtValidationException("Token invalido.");
        }
    }

    private String stringClaim(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        if (value == null || value.toString().isBlank()) {
            throw new JwtValidationException("Token invalido.");
        }
        return value.toString();
    }

    private long numberClaim(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        if (value instanceof Number number) {
            return number.longValue();
        }
        throw new JwtValidationException("Token invalido.");
    }

    private String encode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private String sign(String value) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
        return encode(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    }
}
