package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "DetailsAccountRequest")
public class DetailsAccountRequest {
    private Long idAccount;

    public DetailsAccountRequest() {
    }

    public DetailsAccountRequest(Long idAccount) {
        this.idAccount = idAccount;
    }
}
