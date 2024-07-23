package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for responses containing a token.
 */
@Data
@XmlRootElement(name = "TokenResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TokenResponse {
    @XmlElement(required = true)
    private String token;

    public TokenResponse() {
    }

    public TokenResponse(String token) {
        this.token = token;
    }
}