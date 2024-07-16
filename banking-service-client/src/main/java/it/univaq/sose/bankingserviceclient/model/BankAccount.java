package it.univaq.sose.bankingserviceclient.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccount {
    private Long id;

    private String iban;

    private BigDecimal balance;

    public BankAccount(Long id, String iban, BigDecimal balance) {
        this.id = id;
        this.iban = iban;
        this.balance = balance;
    }
}
