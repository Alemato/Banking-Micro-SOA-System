package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for requests to open a new bank account.
 */
@Data
@XmlRootElement(name = "OpenBankAccountRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenBankAccountRequest {
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String surname;
    @XmlElement(required = true)
    private String username;
    @XmlElement(required = true)
    private String password;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String phone;

    public OpenBankAccountRequest() {
    }

    public OpenBankAccountRequest(String name, String surname, String username, String password, String email, String phone) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
}
