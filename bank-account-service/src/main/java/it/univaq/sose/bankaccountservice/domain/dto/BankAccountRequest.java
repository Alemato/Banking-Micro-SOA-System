package it.univaq.sose.bankaccountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;

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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
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
