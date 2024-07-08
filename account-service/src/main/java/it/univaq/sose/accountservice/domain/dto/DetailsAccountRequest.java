package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "DetailsAccountRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailsAccountRequest {
    @XmlElement(required = true)
    private Long idAccount;

    public DetailsAccountRequest() {
    }

    public DetailsAccountRequest(Long idAccount) {
        this.idAccount = idAccount;
    }
}
