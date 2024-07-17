package it.univaq.sose.bankingserviceclient.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpenLoanRequestDTO {
    private BigDecimal amount;
    private Integer termInYears;
    private String borrowerName;
}
