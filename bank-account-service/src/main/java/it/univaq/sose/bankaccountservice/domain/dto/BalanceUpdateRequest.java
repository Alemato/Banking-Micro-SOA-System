package it.univaq.sose.bankaccountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;


@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionRequest", propOrder = {"amount", "accountId"})
public class BalanceUpdateRequest {

    @XmlElement(required = true)
    private BigDecimal amount;

    @XmlElement(required = true)
    private Long accountId;

    public BalanceUpdateRequest() {
    }

    public BalanceUpdateRequest(BigDecimal amount, Long accountId) {
        this.amount = amount;
        this.accountId = accountId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BalanceUpdateRequest that)) return false;

        return Objects.equals(amount, that.amount) && Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(amount);
        result = 31 * result + Objects.hashCode(accountId);
        return result;
    }
}
