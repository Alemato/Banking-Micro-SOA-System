package it.univaq.sose.transactionserviceprosumer.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BankAccountTransactionResponse")
public class BankAccountTransactionResponse {

    @XmlElement(required = true)
    private Long id;

    @XmlElement(required = true)
    private Long accountId;

    @XmlElement(required = true)
    private String iban;

    @XmlElement(required = true)
    private BigDecimal balance;


    public BankAccountTransactionResponse() {
    }

    public BankAccountTransactionResponse(Long id, Long accountId, String iban, BigDecimal balance) {
        this.id = id;
        this.accountId = accountId;
        this.iban = iban;
        this.balance = balance;
    }
}
