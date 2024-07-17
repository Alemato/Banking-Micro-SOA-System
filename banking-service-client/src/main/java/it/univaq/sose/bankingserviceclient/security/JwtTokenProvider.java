package it.univaq.sose.bankingserviceclient.security;

import org.apache.cxf.rs.security.jose.jws.JwsJwtCompactConsumer;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private String token;

    public synchronized String getToken() {
        return token;
    }

    public synchronized void setToken(String token) {
        this.token = token;
    }

    public synchronized void clearToken() {
        this.token = null;
    }

    public JwtClaims decodeToken() {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is null or empty");
        }
        JwsJwtCompactConsumer jwtConsumer = new JwsJwtCompactConsumer(token);

        return jwtConsumer.getJwtClaims();
    }
}
