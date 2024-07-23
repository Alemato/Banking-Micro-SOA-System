package it.univaq.sose.gatewayservice.domain;

import lombok.Data;

/**
 * A request containing a JWT token.
 */
@Data
public class TokenRequest {
    private String token;

    public TokenRequest() {
    }

    public TokenRequest(String token) {
        this.token = token;
    }
}
