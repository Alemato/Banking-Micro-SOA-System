package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "TokenResponse")
public class TokenResponse {
    private String token;

    public TokenResponse() {
    }

    public TokenResponse(String token) {
        this.token = token;
    }
}






















