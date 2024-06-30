package it.univaq.sose.accountservice.security;

import org.apache.cxf.rs.security.jose.jws.JwsCompactConsumer;
import org.apache.cxf.rs.security.jose.jws.JwsSignatureVerifier;
import org.apache.cxf.rs.security.jose.jwt.JoseJwtConsumer;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.apache.cxf.rs.security.jose.jwt.JwtToken;


public class JWTVerify {

    private JWTVerify() {
        throw new IllegalStateException("JWTVerify JWT Utility class");
    }

    public static boolean verifyJwtToken(String jwtToken) {
        JwsCompactConsumer consumer = new JwsCompactConsumer(jwtToken);
        JwsSignatureVerifier verifier = JWTSignatureProviderVerifier.getSignatureVerifier();
        boolean signatureValid = consumer.verifySignatureWith(verifier);


        if (signatureValid) {
            // Estrai e utilizza le richieste JWT se necessario
            JoseJwtConsumer jwtConsumer = new JoseJwtConsumer();
            JwtToken token = jwtConsumer.getJwtToken(jwtToken);
            JwtClaims claims = token.getClaims();

            // Puoi anche fare ulteriori verifiche sulle richieste del token
            // Esempio: controlla se il token è scaduto
            long currentTimeInSecs = System.currentTimeMillis() / 1000L;
            if (claims.getExpiryTime() != null && claims.getExpiryTime() < currentTimeInSecs) {
                return false; // Token scaduto
            }

            return true; // Firma valida e token non scaduto
        }
        return false;
    }
}
