package it.univaq.sose.bankingserviceclient.security;

import org.apache.cxf.rs.security.jose.jws.JwsJwtCompactConsumer;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.springframework.stereotype.Component;

/**
 * A component for managing JWT tokens, including storing, clearing, and decoding the token.
 */
@Component
public class JwtTokenProvider {
    private String token;

    /**
     * Retrieves the stored JWT token.
     *
     * @return the stored JWT token
     */
    public synchronized String getToken() {
        return token;
    }

    /**
     * Sets a new JWT token.
     *
     * @param token the new JWT token to be stored
     */
    public synchronized void setToken(String token) {
        this.token = token;
    }

    /**
     * Clears the stored JWT token.
     */
    public synchronized void clearToken() {
        this.token = null;
    }

    /**
     * Decodes the stored JWT token into JwtClaims.
     *
     * @return the decoded JwtClaims
     * @throws IllegalArgumentException if the token is null or empty
     */
    public JwtClaims decodeToken() {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is null or empty");
        }
        JwsJwtCompactConsumer jwtConsumer = new JwsJwtCompactConsumer(token);

        return jwtConsumer.getJwtClaims();
    }
}
