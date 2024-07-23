package it.univaq.sose.transactionserviceprosumer.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for creating a bancomat card.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CreateBancomatResponse")
public class CreateBancomatResponse {
    @XmlElement(required = true)
    protected long id;
    @XmlElement(required = true)
    protected String number;
    @XmlElement(required = true)
    protected String cvv;
    @XmlElement(required = true)
    protected String dataScadenza;

    public CreateBancomatResponse() {
    }

    public CreateBancomatResponse(long id, String number, String cvv, String dataScadenza) {
        this.id = id;
        this.number = number;
        this.cvv = cvv;
        this.dataScadenza = dataScadenza;
    }
}
