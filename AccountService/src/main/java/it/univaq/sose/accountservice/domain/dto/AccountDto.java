package it.univaq.sose.accountservice.domain.dto;

import it.univaq.sose.accountservice.domain.Role;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link it.univaq.sose.accountservice.domain.Account}
 */
@Value
@XmlRootElement(name = "AccountResponse")
public class AccountDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 4592896323731902686L;

    Long id;
    LocalDateTime createDate;
    LocalDateTime updateDate;
    String name;
    String surname;
    String username;
    Role role;
    Long idBankAccount;
}