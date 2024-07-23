package it.univaq.sose.loanserviceprosumer.domain.dto;

import it.univaq.sose.loanserviceprosumer.domain.LoanStatus;
import it.univaq.sose.loanserviceprosumer.domain.dto.adapter.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link it.univaq.sose.loanserviceprosumer.domain.Loan} Response
 */
@Data
@XmlRootElement(name = "Loan")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 8189681449897126969L;
    @XmlElement(required = true)
    Long id;
    @XmlElement(required = true)
    BigDecimal amount;
    @XmlElement(required = true)
    Double interestRate;
    @XmlElement(required = true)
    Integer termInYears;
    @XmlElement(required = true)
    String borrowerName;
    @XmlElement(required = true)
    LoanStatus loanStatus;
    @XmlElement(required = true)
    Long idBankAccount;
    @XmlElement(required = true)
    Long idAccount;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    LocalDateTime createDate;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    LocalDateTime updateDate;

    public LoanDto() {
    }

    public LoanDto(Long id, BigDecimal amount, Double interestRate, Integer termInYears, String borrowerName, LoanStatus loanStatus, Long idBankAccount, Long idAccount, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.amount = amount;
        this.interestRate = interestRate;
        this.termInYears = termInYears;
        this.borrowerName = borrowerName;
        this.loanStatus = loanStatus;
        this.idBankAccount = idBankAccount;
        this.idAccount = idAccount;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}