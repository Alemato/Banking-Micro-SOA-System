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
    private Long accountId;

    @XmlElement(required = true)
    private BigDecimal amount;

    public BalanceUpdateRequest() {
    }

    public BalanceUpdateRequest(BigDecimal amount, Long accountId) {
        this.amount = amount;
        this.accountId = accountId;
    }
}
