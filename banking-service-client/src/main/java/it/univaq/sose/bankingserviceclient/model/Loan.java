package it.univaq.sose.bankingserviceclient.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Loan {
    private Long id;

    private BigDecimal amount;

    private Double interestRate;

    private Integer termInYears;

    private String borrowerName;

    public Loan(Long id, BigDecimal amount, Double interestRate, Integer termInYears, String borrowerName) {
        this.id = id;
        this.amount = amount;
        this.interestRate = interestRate;
        this.termInYears = termInYears;
        this.borrowerName = borrowerName;
    }
}
