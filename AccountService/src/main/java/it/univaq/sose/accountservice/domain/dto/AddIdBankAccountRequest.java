package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "AddIdBankAccountRequest")
public class AddIdBankAccountRequest {
    private Long idAccount;
    private Long idBankAccount;

    public AddIdBankAccountRequest() {
    }

    public AddIdBankAccountRequest(Long idAccount, Long idBankAccount) {
        this.idAccount = idAccount;
        this.idBankAccount = idBankAccount;
    }
}
