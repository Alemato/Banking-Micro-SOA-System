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

/**
 * Data Transfer Object (DTO) for transfer requests.
 */
@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransferRequest", propOrder = {"senderAccountId", "receiverAccountId", "amount", "description"})
public class TransferRequest {

    @XmlElement(required = true)
    private Long senderAccountId;

    @XmlElement(required = true)
    private Long receiverAccountId;

    @XmlElement(required = true)
    private BigDecimal amount;

    @XmlElement(required = true)
    private String description;

    public TransferRequest() {
    }

    public TransferRequest(Long senderAccountId, Long receiverAccountId, BigDecimal amount, String description) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferRequest that = (TransferRequest) o;
        return Objects.equals(senderAccountId, that.senderAccountId) && Objects.equals(receiverAccountId, that.receiverAccountId) && Objects.equals(amount, that.amount) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(senderAccountId);
        result = 31 * result + Objects.hashCode(receiverAccountId);
        result = 31 * result + Objects.hashCode(amount);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }
}
