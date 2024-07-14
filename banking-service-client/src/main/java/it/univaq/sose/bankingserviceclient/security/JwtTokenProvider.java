package it.univaq.sose.bankingserviceclient.security;

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
}
