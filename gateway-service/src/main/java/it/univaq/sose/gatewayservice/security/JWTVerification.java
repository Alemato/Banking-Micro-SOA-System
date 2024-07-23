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

/**
 * Service class for handling JWT token verification and role checks.
 */
@Slf4j
@Service
public class JWTVerification {
    // Secret key used for signing the JWT tokens
    private static final String SECRET_KEY = "3f8bRb6!fc84c8#8^aB5Df99*45d&Ef2";

    /**
     * Verifies the given JWT token for signature validity and expiry.
     *
     * @param token the JWT token to be verified.
     * @throws AuthenticationException if the token is invalid or expired.
     */
    public void verifyToken(String token) throws AuthenticationException {
        // Create a JWS Compact Consumer for the provided token
        JwsCompactConsumer consumer = new JwsCompactConsumer(token);
        // Create a signature verifier using HMAC with SHA-256
        JwsSignatureVerifier verifier = new HmacJwsSignatureVerifier(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256);
        // Verify the token signature
        boolean signatureValid = consumer.verifySignatureWith(verifier);

        if (!signatureValid) {
            log.error("Token Verification failed");
            throw new AuthenticationException("Token Verification failed");
        }

        JwsJwtCompactConsumer jwtConsumer = new JwsJwtCompactConsumer(token);
        // Create a JWT Compact Consumer to handle JWT specifics
        JwtClaims claims = jwtConsumer.getJwtClaims();

        // Check if the token has expired
        long currentTimeInSecs = System.currentTimeMillis() / 1000L;
        if (claims.getExpiryTime() != null && claims.getExpiryTime() < currentTimeInSecs) {
            log.error("Token Verification failed");
            throw new AuthenticationException("Token Verification failed");
        }
    }

    /**
     * Checks if the token contains a role that matches the allowed roles.
     *
     * @param token the JWT token to be checked.
     * @param roles the list of allowed roles.
     * @throws AuthenticationException if the role is not allowed.
     */
    public void checkRoles(String token, List<String> roles) throws AuthenticationException {
        JwsJwtCompactConsumer jwtConsumer = new JwsJwtCompactConsumer(token);
        String role = (String) jwtConsumer.getJwtClaims().getClaim("role");
        if (role == null || !roles.contains(role)) {
            log.error("Forbidden: role not allowed: {} in Roles: {}", role, roles);
            throw new AuthenticationException("Forbidden: role not allowed");
        }
    }

    /**
     * Retrieves the role from the JWT token.
     *
     * @param token the JWT token.
     * @return the role extracted from the token.
     * @throws AuthenticationException if the role is not found.
     */
    public String getRole(String token) throws AuthenticationException {
        JwsJwtCompactConsumer jwtConsumer = new JwsJwtCompactConsumer(token);
        String role = (String) jwtConsumer.getJwtClaims().getClaim("role");
        if (role == null) {
            log.error("role not found");
            throw new AuthenticationException("role not found");
        }
        return role;
    }

    /**
     * Retrieves the identifier from the JWT token.
     *
     * @param token the JWT token.
     * @return the identifier extracted from the token.
     * @throws AuthenticationException if the identifier is not found.
     */

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
