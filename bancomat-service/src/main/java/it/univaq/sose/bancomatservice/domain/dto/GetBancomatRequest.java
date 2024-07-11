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
@XmlType(name = "GetBancomatDetailsRequest", propOrder = {"accountId"})
public class GetBancomatRequest {

    @XmlElement(required = true)
    private Long accountId;

    public GetBancomatRequest() {
    }

    public GetBancomatRequest(Long accountId) {
        this.accountId = accountId;
    }
}
