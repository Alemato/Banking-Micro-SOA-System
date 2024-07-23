package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for adding a bank account ID to an account.
 */
@Data
@XmlRootElement(name = "AddIdBankAccountRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddIdBankAccountRequest {
    @XmlElement(required = true)
    private Long idBankAccount;

    public AddIdBankAccountRequest() {
    }

    public AddIdBankAccountRequest(Long idBankAccount) {
        this.idBankAccount = idBankAccount;
    }
}
