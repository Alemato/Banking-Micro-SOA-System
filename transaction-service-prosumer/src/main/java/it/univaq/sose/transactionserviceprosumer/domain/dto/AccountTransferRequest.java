package it.univaq.sose.transactionserviceprosumer.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for a transfer request.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AccountTransferRequest")
public class AccountTransferRequest {

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private String surname;

    @XmlElement(required = true)
    private String iban;

    public AccountTransferRequest() {
    }

    public AccountTransferRequest(String name, String surname, String iban) {
        this.name = name;
        this.surname = surname;
        this.iban = iban;
    }
}
