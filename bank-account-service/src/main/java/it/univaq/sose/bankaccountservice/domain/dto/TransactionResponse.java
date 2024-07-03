package it.univaq.sose.bankaccountservice.domain.dto;

import it.univaq.sose.bankaccountservice.domain.TransactionType;
import it.univaq.sose.bankaccountservice.domain.dto.adapter.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionResponse", propOrder = {"id", "transactionCode", "amount", "description", "transactionType", "date", "bankAccountSender", "bankAccountReceiver"})
public class TransactionResponse {

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

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement(required = true)
    private LocalDateTime date;

    @XmlElement(required = true)
    private BankAccountResponse bankAccountSender;

    @XmlElement(required = true)
    private BankAccountResponse bankAccountReceiver;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, String transactionCode, BigDecimal amount, String description, TransactionType transactionType, LocalDateTime date, BankAccountResponse bankAccountSender, BankAccountResponse bankAccountReceiver) {
        this.id = id;
        this.transactionCode = transactionCode;
        this.amount = amount;
        this.description = description;
        this.transactionType = transactionType;
        this.date = date;
        this.bankAccountSender = bankAccountSender;
        this.bankAccountReceiver = bankAccountReceiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionResponse that = (TransactionResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(transactionCode, that.transactionCode) && Objects.equals(amount, that.amount) && Objects.equals(description, that.description) && transactionType == that.transactionType && Objects.equals(date, that.date) && Objects.equals(bankAccountSender, that.bankAccountSender) && Objects.equals(bankAccountReceiver, that.bankAccountReceiver);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(transactionCode);
        result = 31 * result + Objects.hashCode(amount);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(transactionType);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(bankAccountSender);
        result = 31 * result + Objects.hashCode(bankAccountReceiver);
        return result;
    }
}
