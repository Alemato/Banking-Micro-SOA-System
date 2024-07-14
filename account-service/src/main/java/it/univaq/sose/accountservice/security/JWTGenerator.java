package it.univaq.sose.accountservice.security;

import it.univaq.sose.accountservice.domain.Role;
import org.apache.cxf.rs.security.jose.common.JoseType;
import org.apache.cxf.rs.security.jose.jws.JwsHeaders;
import org.apache.cxf.rs.security.jose.jws.JwsJwtCompactProducer;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.apache.cxf.rs.security.jose.jwt.JwtToken;


public class JWTGenerator {

    private JWTGenerator() {
        throw new IllegalStateException("JWTGenerator JWT Utility class");
    }

    public static String createJwtToken(String username, long idAccount, Role role) {
        JwsHeaders headers = new JwsHeaders(JoseType.JWT, JWTSignatureProviderVerifier.getSignatureAlgorithm());

        JwtClaims claims = new JwtClaims();
        claims.setSubject(username);// The user for whom the token is valid
        claims.setClaim("identifier", idAccount);
        claims.setClaim("role", role.toString()); // The role in string
        claims.setIssuedAt(System.currentTimeMillis() / 1000);
        claims.setExpiryTime((System.currentTimeMillis() / 1000) + 3600);  // Token valid for 1 H


        JwtToken token = new JwtToken(headers, claims);

        JwsJwtCompactProducer producer = new JwsJwtCompactProducer(token);
        return producer.signWith(JWTSignatureProviderVerifier.getSignatureProvider());
    }
}
