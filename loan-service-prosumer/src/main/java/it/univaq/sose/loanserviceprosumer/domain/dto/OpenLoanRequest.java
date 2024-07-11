package it.univaq.sose.loanserviceprosumer.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.math.BigDecimal;

@Data
@XmlRootElement(name = "OpenLoanRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenLoanRequest {
    @XmlElement(required = true)
    BigDecimal amount;
    @XmlElement(required = true)
    Double interestRate;
    @XmlElement(required = true)
    Integer termInYears;
    @XmlElement(required = true)
    String borrowerName;
    @XmlElement(required = true)
    Long idBankAccount;
}
