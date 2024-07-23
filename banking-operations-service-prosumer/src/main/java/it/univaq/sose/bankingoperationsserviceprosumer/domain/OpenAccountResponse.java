package it.univaq.sose.bankingoperationsserviceprosumer.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for the response of an open account request.
 * This class contains all the necessary information provided in the response after opening a new account.
 */
@Data
@XmlRootElement(name = "OpenAccountResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenAccountResponse {
    @XmlElement(required = true)
    private long id;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String surname;
    @XmlElement(required = true)
    private String username;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private long bankAccountId;
    @XmlElement(required = true)
    private String iban;
    @XmlElement(required = true)
    private BigDecimal balance;
    @XmlElement(required = true)
    private long bancomatId;
    @XmlElement(required = true)
    private String bancomatNumber;
    @XmlElement(required = true)
    private String bancomatCvv;
    @XmlElement(required = true)
    private String bancomatExpiryDate;
}
