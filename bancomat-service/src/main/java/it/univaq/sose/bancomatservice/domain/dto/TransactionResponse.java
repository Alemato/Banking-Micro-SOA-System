package it.univaq.sose.bancomatservice.domain.dto;

import it.univaq.sose.bancomatservice.domain.dto.adapter.LocalDateTimeAdapter;
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

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionResponse", propOrder = {"id", "transactionCode", "amount", "description", "date"})
public class TransactionResponse {

    @XmlElement(required = true)
    private Long id;

    @XmlElement(required = true)
    private String transactionCode;

    @XmlElement(required = true)
    private BigDecimal amount;

    @XmlElement(required = true)
    private String description;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement(required = true)
    private LocalDateTime date;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, String transactionCode, BigDecimal amount, String description, LocalDateTime date) {
        this.id = id;
        this.transactionCode = transactionCode;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
}
