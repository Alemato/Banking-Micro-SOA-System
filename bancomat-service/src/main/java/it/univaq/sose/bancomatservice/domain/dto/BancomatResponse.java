package it.univaq.sose.bancomatservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BancomatResponse", propOrder = {"id", "number", "cvv", "dataScadenza", "accountId"})
public class BancomatResponse {

    @XmlElement(required = true)
    private Long id;

    @XmlElement(required = true)
    private String number;

    @XmlElement(required = true)
    private String cvv;

    @XmlElement(required = true)
    private String dataScadenza;

    @XmlElement(required = true)
    private Long accountId;

    public BancomatResponse() {
    }

    public BancomatResponse(Long id, String number, String cvv, String dataScadenza, Long accountId) {
        this.id = id;
        this.number = number;
        this.cvv = cvv;
        this.dataScadenza = dataScadenza;
        this.accountId = accountId;
    }
}
