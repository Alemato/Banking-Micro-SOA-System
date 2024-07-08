package it.univaq.sose.accountservice.domain.dto;

import it.univaq.sose.accountservice.domain.Role;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link it.univaq.sose.accountservice.domain.Account}
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
    private Role role;
    @XmlElement(required = true)
    private Long idBankAccount;
    @XmlElement(required = true)
    private LocalDateTime createDate;
    @XmlElement(required = true)
    private LocalDateTime updateDate;

    public AccountResponse() {
    }

    public AccountResponse(Long id, String name, String surname, String username, Role role, Long idBankAccount, LocalDateTime updateDate, LocalDateTime createDate) {
        this.id = id;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.role = role;
        this.idBankAccount = idBankAccount;
    }
}