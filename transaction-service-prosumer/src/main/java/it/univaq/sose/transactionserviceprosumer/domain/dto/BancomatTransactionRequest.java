package it.univaq.sose.transactionserviceprosumer.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.math.BigDecimal;

@Data
@XmlRootElement(name = "BancomatTransactionRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class BancomatTransactionRequest {
    @XmlElement(required = true)
    protected String number;

    @XmlElement(required = true)
    protected BigDecimal amount;

    @XmlElement(required = true)
    protected String description;

    public BancomatTransactionRequest() {
    }

    public BancomatTransactionRequest(String number, BigDecimal amount, String description) {
        this.number = number;
        this.amount = amount;
        this.description = description;
    }
}
