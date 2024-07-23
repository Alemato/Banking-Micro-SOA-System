package it.univaq.sose.transactionserviceprosumer.domain.dto;


import it.univaq.sose.transactionserviceprosumer.domain.TransactionType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for executing transaction response.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TransactionExecuteResponse")
public class ExecuteTransactionResponse {
    @XmlElement(required = true)
    private Long id;

    @XmlElement(required = true)
    private String transactionCode;

    @XmlElement(required = true)
    private BigDecimal amount;

    @XmlElement(required = true)
    private String description;

    @XmlElement(required = true)
    private TransactionType transactionType;

    @XmlElement(required = true)
    private LocalDateTime date;

    @XmlElement()
    protected BankAccountTransactionResponse bankAccountSender;

    @XmlElement()
    protected BankAccountTransactionResponse bankAccountReceiver;

    public ExecuteTransactionResponse() {
    }

    public ExecuteTransactionResponse(Long id, String transactionCode, BigDecimal amount, String description, TransactionType transactionType, LocalDateTime date, BankAccountTransactionResponse bankAccountSender, BankAccountTransactionResponse bankAccountReceiver) {
        this.id = id;
        this.transactionCode = transactionCode;
        this.amount = amount;
        this.description = description;
        this.transactionType = transactionType;
        this.date = date;
        this.bankAccountSender = bankAccountSender;
        this.bankAccountReceiver = bankAccountReceiver;
    }
}
