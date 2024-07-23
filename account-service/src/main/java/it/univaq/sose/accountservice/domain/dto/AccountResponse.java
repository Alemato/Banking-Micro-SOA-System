package it.univaq.sose.accountservice.domain.dto;

import it.univaq.sose.accountservice.domain.Role;
import it.univaq.sose.accountservice.domain.dto.adapter.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for {@link it.univaq.sose.accountservice.domain.Account}.
 */
@Data
@XmlRootElement(name = "AccountResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 4592896323731902686L;
    @XmlElement(required = true)
    private Long id;
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
    private Role role;
    @XmlElement(required = true)
    private Long idBankAccount;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime updateDate;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime createDate;

    public AccountResponse() {
    }

    public AccountResponse(Long id, String name, String surname, String username, String email, String phone, Role role, Long idBankAccount, LocalDateTime updateDate, LocalDateTime createDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.idBankAccount = idBankAccount;
        this.updateDate = updateDate;
        this.createDate = createDate;
    }
}