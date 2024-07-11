package it.univaq.sose.loanserviceprosumer.business.impl;

import it.univaq.sose.loanserviceprosumer.business.LoanManager;
import it.univaq.sose.loanserviceprosumer.domain.Loan;
import it.univaq.sose.loanserviceprosumer.domain.LoanStatus;
import it.univaq.sose.loanserviceprosumer.domain.dto.LoanDto;
import it.univaq.sose.loanserviceprosumer.domain.dto.OpenLoanRequest;
import it.univaq.sose.loanserviceprosumer.repository.LoanRepository;
import it.univaq.sose.loanserviceprosumer.service.LoanNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoanManagerImpl implements LoanManager {

    private final LoanRepository repository;

    public LoanManagerImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public LoanDto openLoan(OpenLoanRequest request) {
        Loan loan = new Loan();
        loan.setAmount(request.getAmount());
        loan.setInterestRate(request.getInterestRate() > 100 ? 100.00 : request.getInterestRate());
        loan.setTermInYears(request.getTermInYears());
        loan.setBorrowerName(request.getBorrowerName());
        loan.setLoanStatus(LoanStatus.APPROVED);
        loan.setIdBankAccount(request.getIdBankAccount());
        loan = repository.save(loan);
        return new LoanDto(loan.getId(), loan.getAmount(), loan.getInterestRate(), loan.getTermInYears(), loan.getBorrowerName(), loan.getLoanStatus(), loan.getIdBankAccount(), loan.getCreateDate(), loan.getUpdateDate());
    }

    @Override
    @Transactional
    public List<LoanDto> getAllLoanByIdBankAccount(long idBankAccount) {
        return repository.findByIdBankAccountOrderByCreateDateDesc(idBankAccount);
    }

    @Override
    @Transactional
    public LoanDto closeLoanByIdLoan(long idLoan) throws LoanNotFoundException {
        Loan loan = repository.findById(idLoan).orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        loan.setLoanStatus(LoanStatus.CLOSED);
        loan = repository.save(loan);
        return new LoanDto(loan.getId(), loan.getAmount(), loan.getInterestRate(), loan.getTermInYears(), loan.getBorrowerName(), loan.getLoanStatus(), loan.getIdBankAccount(), loan.getCreateDate(), loan.getUpdateDate());
    }
}
