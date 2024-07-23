package it.univaq.sose.transactionserviceprosumer.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for updating the bank account balance.
 */
@Data
@XmlRootElement(name = "BancomatTransactionRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class BancomatTransactionResponse {
    @XmlElement(required = true)
    private long id;

    @XmlElement(required = true)
    private String transactionCode;

    @XmlElement(required = true)
    private BigDecimal amount;

    @XmlElement(required = true)
    private String description;

    @XmlElement(required = true)
    private String date;

    @XmlElement(required = true)
    private BankAccountTransactionResponse bankAccountTransactionResponse;

    public BancomatTransactionResponse() {
    }

    public BancomatTransactionResponse(long id, String transactionCode, BigDecimal amount, String description, String date, BankAccountTransactionResponse bankAccountTransactionResponse) {
        this.id = id;
        this.transactionCode = transactionCode;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.bankAccountTransactionResponse = bankAccountTransactionResponse;
    }
}
