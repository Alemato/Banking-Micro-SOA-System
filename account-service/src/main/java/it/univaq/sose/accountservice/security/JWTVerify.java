package it.univaq.sose.accountservice.security;

import org.apache.cxf.rs.security.jose.jws.JwsCompactConsumer;
import org.apache.cxf.rs.security.jose.jws.JwsSignatureVerifier;
import org.apache.cxf.rs.security.jose.jwt.JoseJwtConsumer;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.apache.cxf.rs.security.jose.jwt.JwtToken;

/**
 * Utility class for verifying JWT tokens.
 */
public class JWTVerify {

    private JWTVerify() {
        throw new IllegalStateException("JWTVerify JWT Utility class");
    }

    /**
     * Verifies the validity of a JWT token.
     *
     * @param jwtToken the JWT token to verify
     * @return true if the token is valid, false otherwise
     */
    public static boolean verifyJwtToken(String jwtToken) {
        // Create a JWS compact consumer for the given token
        JwsCompactConsumer consumer = new JwsCompactConsumer(jwtToken);
        // Get the signature verifier
        JwsSignatureVerifier verifier = JWTSignatureProviderVerifier.getSignatureVerifier();
        // Verify the signature of the token
        boolean signatureValid = consumer.verifySignatureWith(verifier);


        if (signatureValid) {
            // Extract and use JWT claims if necessary
            JoseJwtConsumer jwtConsumer = new JoseJwtConsumer();
            JwtToken token = jwtConsumer.getJwtToken(jwtToken);
            JwtClaims claims = token.getClaims();

            // Additional verification can be done on the claims
            // Example: check if the token is expired
            long currentTimeInSecs = System.currentTimeMillis() / 1000L;
            if (claims.getExpiryTime() != null && claims.getExpiryTime() < currentTimeInSecs) {
                return false; // Token is expired
            }

            return true; // Signature is valid and token is not expired
        }
        return false;// Signature is not valid
    }
}
