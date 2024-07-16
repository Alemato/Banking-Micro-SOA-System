package it.univaq.sose.bankingoperationsserviceprosumer.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.math.BigDecimal;

@Data
@XmlRootElement(name = "BankAccountDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankAccountDetails {
    @XmlElement(required = true)
    private long id;
    @XmlElement(required = true)
    private long accountId;
    @XmlElement(required = true)
    private String iban;
    @XmlElement(required = true)
    private BigDecimal balance;
}
