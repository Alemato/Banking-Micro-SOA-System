package it.univaq.sose.accountservice.domain.dto;

import it.univaq.sose.accountservice.domain.Role;
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
public class AccountResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 4592896323731902686L;
    private Long id;
    private String name;
    private String surname;
    private String username;
    private Role role;
    private Long idBankAccount;
    private LocalDateTime createDate;
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