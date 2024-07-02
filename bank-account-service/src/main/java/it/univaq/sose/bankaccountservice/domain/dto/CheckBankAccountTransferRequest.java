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
@XmlType(name = "CheckBankAccountTransferRequest", propOrder = {"senderIban", "receiverIban", "amount"})
public class CheckBankAccountTransferRequest {

    @XmlElement(required = true)
    private String senderIban;

    @XmlElement(required = true)
    private String receiverIban;

    @XmlElement(required = true)
    private BigDecimal amount;

    public CheckBankAccountTransferRequest() {
    }

    public CheckBankAccountTransferRequest(String senderIban, String receiverIban, BigDecimal amount) {
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckBankAccountTransferRequest that = (CheckBankAccountTransferRequest) o;
        return Objects.equals(senderIban, that.senderIban) && Objects.equals(receiverIban, that.receiverIban) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(senderIban);
        result = 31 * result + Objects.hashCode(receiverIban);
        result = 31 * result + Objects.hashCode(amount);
        return result;
    }
}
