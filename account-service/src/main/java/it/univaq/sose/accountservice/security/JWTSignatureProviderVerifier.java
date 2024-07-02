package it.univaq.sose.accountservice.security;

import org.apache.cxf.rs.security.jose.jwa.SignatureAlgorithm;
import org.apache.cxf.rs.security.jose.jws.HmacJwsSignatureProvider;
import org.apache.cxf.rs.security.jose.jws.HmacJwsSignatureVerifier;
import org.apache.cxf.rs.security.jose.jws.JwsSignatureProvider;
import org.apache.cxf.rs.security.jose.jws.JwsSignatureVerifier;

public class JWTSignatureProviderVerifier {
    private static final String SECRET_KEY = "3f8bRb6!fc84c8#8^aB5Df99*45d&Ef2";

    private JWTSignatureProviderVerifier() {
        throw new IllegalStateException("JWTSignatureProviderVerifier JWT Utility class");
    }

    public static SignatureAlgorithm getSignatureAlgorithm() {
        return SignatureAlgorithm.HS256;
    }

    public static JwsSignatureProvider getSignatureProvider() {
        return new HmacJwsSignatureProvider(SECRET_KEY.getBytes(), getSignatureAlgorithm());
    }

    public static JwsSignatureVerifier getSignatureVerifier() {
        return new HmacJwsSignatureVerifier(SECRET_KEY.getBytes(), getSignatureAlgorithm());
    }
}
