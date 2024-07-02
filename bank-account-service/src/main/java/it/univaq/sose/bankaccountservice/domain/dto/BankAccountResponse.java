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

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankAccountResponse", propOrder = {"id", "accountId", "iban", "balance"})
public class BankAccountResponse {

    @XmlElement(required = true)
    private Long id;

    @XmlElement(required = true)
    private Long accountId;

    @XmlElement(required = true)
    private String iban;

    @XmlElement(required = true)
    private BigDecimal balance;

    public BankAccountResponse() {
    }

    public BankAccountResponse(Long id, Long accountId, String iban, BigDecimal balance) {
        this.id = id;
        this.accountId = accountId;
        this.iban = iban;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankAccountResponse that = (BankAccountResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(accountId, that.accountId) && Objects.equals(iban, that.iban) && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(accountId);
        result = 31 * result + Objects.hashCode(iban);
        result = 31 * result + Objects.hashCode(balance);
        return result;
    }
}
