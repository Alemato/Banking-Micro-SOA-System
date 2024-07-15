package it.univaq.sose.gatewayservice.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.rs.security.jose.jwa.SignatureAlgorithm;
import org.apache.cxf.rs.security.jose.jws.HmacJwsSignatureVerifier;
import org.apache.cxf.rs.security.jose.jws.JwsCompactConsumer;
import org.apache.cxf.rs.security.jose.jws.JwsJwtCompactConsumer;
import org.apache.cxf.rs.security.jose.jws.JwsSignatureVerifier;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class JWTVerification {
    private static final String SECRET_KEY = "3f8bRb6!fc84c8#8^aB5Df99*45d&Ef2";

    public void verifyToken(String token) throws AuthenticationException {
        JwsCompactConsumer consumer = new JwsCompactConsumer(token);
        JwsSignatureVerifier verifier = new HmacJwsSignatureVerifier(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256);
        boolean signatureValid = consumer.verifySignatureWith(verifier);

        if (!signatureValid) {
            log.error("Token Verification failed");
            throw new AuthenticationException("Token Verification failed");
        }

        JwsJwtCompactConsumer jwtConsumer = new JwsJwtCompactConsumer(token);
        // Estrai e utilizza le richieste JWT se necessario
        JwtClaims claims = jwtConsumer.getJwtClaims();

        // Puoi anche fare ulteriori verifiche sulle richieste del token
        // Esempio: controlla se il token è scaduto
        long currentTimeInSecs = System.currentTimeMillis() / 1000L;
        if (claims.getExpiryTime() != null && claims.getExpiryTime() < currentTimeInSecs) {
            log.error("Token Verification failed");
            throw new AuthenticationException("Token Verification failed");
        }
    }


    public void checkRoles(String token, List<String> roles) throws AuthenticationException {
        JwsJwtCompactConsumer jwtConsumer = new JwsJwtCompactConsumer(token);
        String role = (String) jwtConsumer.getJwtClaims().getClaim("role");
        if (role == null || !roles.contains(role)) {
            log.error("Forbidden: role not allowed: {} in Roles: {}", role, roles);
            throw new AuthenticationException("Forbidden: role not allowed");
        }
    }

    public String getRole(String token) throws AuthenticationException {
        JwsJwtCompactConsumer jwtConsumer = new JwsJwtCompactConsumer(token);
        String role = (String) jwtConsumer.getJwtClaims().getClaim("role");
        if (role == null) {
            log.error("role not found");
            throw new AuthenticationException("role not found");
        }
        return role;
    }

    public long getIdentifier(String token) throws AuthenticationException {
        JwsJwtCompactConsumer jwtConsumer = new JwsJwtCompactConsumer(token);
        Object identifier = jwtConsumer.getJwtClaims().getClaim("identifier");
        if (identifier == null) {
            log.error("identifier not found");
            throw new AuthenticationException("identifier not found");
        }
        return Long.parseLong(identifier.toString());
    }
}
