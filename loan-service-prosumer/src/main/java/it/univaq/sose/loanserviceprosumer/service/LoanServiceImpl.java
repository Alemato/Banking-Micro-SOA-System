package it.univaq.sose.loanserviceprosumer.service;

import it.univaq.sose.loanserviceprosumer.business.LoanManager;
import it.univaq.sose.loanserviceprosumer.domain.dto.OpenLoanRequest;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanManager loanManager;

    public LoanServiceImpl(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    @Override
    public void openLoan(OpenLoanRequest openLoanRequest) {

    }
}
