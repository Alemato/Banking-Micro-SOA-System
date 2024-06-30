package it.univaq.sose.accountservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2741904033865180248L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @Enumerated
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "id_bank_account")
    private Long idBankAccount;

}

