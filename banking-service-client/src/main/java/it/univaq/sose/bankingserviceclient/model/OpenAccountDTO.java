package it.univaq.sose.bankingserviceclient.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpenAccountDTO {
    private String name;

    private String surname;

    private String email;

    private String phone;

    private BigDecimal balance;

    private String username;

    private String password;
}
