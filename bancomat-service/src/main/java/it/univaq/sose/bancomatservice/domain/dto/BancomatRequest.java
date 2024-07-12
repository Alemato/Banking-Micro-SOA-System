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
@XmlType(name = "BancomatRequest", propOrder = {"accountId", "bankAccountId"})
public class BancomatRequest {

    @XmlElement(required = true)
    private Long accountId;

    @XmlElement(required = true)
    private Long bankAccountId;

    public BancomatRequest() {
    }

    public BancomatRequest(Long accountId, Long bankAccountId) {
        this.accountId = accountId;
        this.bankAccountId = bankAccountId;
    }
}
