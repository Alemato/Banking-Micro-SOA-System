package it.univaq.sose.loanserviceprosumer.business;

import it.univaq.sose.loanserviceprosumer.domain.dto.LoanDto;
import it.univaq.sose.loanserviceprosumer.domain.dto.OpenLoanRequest;
import it.univaq.sose.loanserviceprosumer.service.LoanNotFoundException;

import java.util.List;

public interface LoanManager {
    LoanDto openLoan(OpenLoanRequest request);

    List<LoanDto> getAllLoanByIdBankAccount(long idBankAccount);

    LoanDto closeLoanByIdLoan(long idLoan) throws LoanNotFoundException;
}
