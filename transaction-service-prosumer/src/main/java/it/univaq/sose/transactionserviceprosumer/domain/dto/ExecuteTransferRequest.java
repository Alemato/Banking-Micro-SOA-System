package it.univaq.sose.transactionserviceprosumer.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for executing a transfer request from a bank account to another one.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ExecuteTransferRequest")
public class ExecuteTransferRequest {

    @XmlElement(required = true)
    private BigDecimal amount;

    @XmlElement(required = true)
    private String description;

    @XmlElement(required = true)
    private AccountTransferRequest senderAccountTransferRequest;

    @XmlElement(required = true)
    private AccountTransferRequest receiverAccountTransferRequest;

    public ExecuteTransferRequest() {
    }

    public ExecuteTransferRequest(BigDecimal amount, String description, AccountTransferRequest senderAccountTransferRequest, AccountTransferRequest receiverAccountTransferRequest) {
        this.amount = amount;
        this.description = description;
        this.senderAccountTransferRequest = senderAccountTransferRequest;
        this.receiverAccountTransferRequest = receiverAccountTransferRequest;
    }
}
