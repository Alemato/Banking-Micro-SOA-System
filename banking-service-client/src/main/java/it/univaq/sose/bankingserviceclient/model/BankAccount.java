package it.univaq.sose.bankingserviceclient.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccount {
    private String iban;

    private BigDecimal balance;

    public BankAccount(String iban, BigDecimal balance) {
        this.iban = iban;
        this.balance = balance;
    }
}
