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

/**
 * Data Transfer Object (DTO) for Bancomat responses.
 */
@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BancomatTransactionResponse", propOrder = {"id", "transactionCode", "amount", "description", "date"})
public class BancomatTransactionResponse {

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

    public BancomatTransactionResponse() {
    }

    public BancomatTransactionResponse(Long id, String transactionCode, BigDecimal amount, String description, LocalDateTime date) {
        this.id = id;
        this.transactionCode = transactionCode;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
}
