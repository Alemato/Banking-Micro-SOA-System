package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "ErrorResponse")
public class ErrorResponse {
    private String error;

    public ErrorResponse() {
    }

    public ErrorResponse(String error) {
        this.error = error;
    }
}
