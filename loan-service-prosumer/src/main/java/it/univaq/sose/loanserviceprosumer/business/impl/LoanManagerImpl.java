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

/**
 * Implementation of the LoanManager interface.
 * This class provides the concrete implementation of the operations for managing loans.
 */
@Service
public class LoanManagerImpl implements LoanManager {

    private final LoanRepository repository;

    public LoanManagerImpl(LoanRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
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
        loan.setIdAccount(request.getIdAccount());
        loan = repository.save(loan);
        return new LoanDto(loan.getId(), loan.getAmount(), loan.getInterestRate(), loan.getTermInYears(), loan.getBorrowerName(), loan.getLoanStatus(), loan.getIdBankAccount(), loan.getIdAccount(), loan.getCreateDate(), loan.getUpdateDate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> getAllLoanByIdBankAccount(long idBankAccount) {
        return repository.findByIdBankAccountOrderByCreateDateDesc(idBankAccount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> getAllLoanByIdAccount(long idAccount) {
        return repository.findByIdAccountOrderByCreateDateDesc(idAccount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LoanDto closeLoanByIdLoan(long idLoan) throws LoanNotFoundException {
        Loan loan = repository.findById(idLoan).orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        loan.setLoanStatus(LoanStatus.CLOSED);
        loan = repository.save(loan);
        return new LoanDto(loan.getId(), loan.getAmount(), loan.getInterestRate(), loan.getTermInYears(), loan.getBorrowerName(), loan.getLoanStatus(), loan.getIdBankAccount(), loan.getIdAccount(), loan.getCreateDate(), loan.getUpdateDate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public LoanDto getLoanById(long idLoan) throws LoanNotFoundException {
        Loan loan = repository.findById(idLoan).orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        return new LoanDto(loan.getId(), loan.getAmount(), loan.getInterestRate(), loan.getTermInYears(), loan.getBorrowerName(), loan.getLoanStatus(), loan.getIdBankAccount(), loan.getIdAccount(), loan.getCreateDate(), loan.getUpdateDate());
    }
}
