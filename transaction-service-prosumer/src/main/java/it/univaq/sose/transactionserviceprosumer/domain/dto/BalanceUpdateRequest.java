package it.univaq.sose.transactionserviceprosumer.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.math.BigDecimal;


@Data
@XmlRootElement(name = "BalanceUpdateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class BalanceUpdateRequest {

    @XmlElement(required = true)
    private Long bankAccountId;

    @XmlElement(required = true)
    private BigDecimal amount;

    @XmlElement(required = true)
    private String description;

    public BalanceUpdateRequest() {
    }

    public BalanceUpdateRequest(Long bankAccountId, BigDecimal amount, String description) {
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.description = description;
    }
}
