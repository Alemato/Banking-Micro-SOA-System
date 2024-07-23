package it.univaq.sose.bankaccountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for bank account creation requests.
 */
@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankAccountRequest", propOrder = {"accountId", "balance"})
public class BankAccountRequest {

    @XmlElement(required = true)
    private Long accountId;

    @XmlElement(required = true)
    private BigDecimal balance;

    public BankAccountRequest() {
    }

    public BankAccountRequest(@NonNull Long accountId, @NonNull BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankAccountRequest that = (BankAccountRequest) o;
        return accountId.equals(that.accountId) && balance.equals(that.balance);
    }

    @Override
    public int hashCode() {
        int result = accountId.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }
}
