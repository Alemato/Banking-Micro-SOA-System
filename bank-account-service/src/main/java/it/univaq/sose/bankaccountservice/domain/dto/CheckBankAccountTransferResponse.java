package it.univaq.sose.bankaccountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CheckBankAccountRequest", propOrder = {"senderAccountId", "receiverAccountId"})
public class CheckBankAccountTransferResponse {

    @XmlElement(required = true)
    private Long senderAccountId;

    @XmlElement(required = true)
    private Long receiverAccountId;

    public CheckBankAccountTransferResponse() {
    }

    public CheckBankAccountTransferResponse(Long senderAccountId, Long receiverAccountId) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckBankAccountTransferResponse that = (CheckBankAccountTransferResponse) o;
        return Objects.equals(senderAccountId, that.senderAccountId) && Objects.equals(receiverAccountId, that.receiverAccountId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(senderAccountId);
        result = 31 * result + Objects.hashCode(receiverAccountId);
        return result;
    }
}
