package it.univaq.sose.loanserviceprosumer.business;

import it.univaq.sose.loanserviceprosumer.domain.dto.LoanDto;
import it.univaq.sose.loanserviceprosumer.domain.dto.OpenLoanRequest;
import it.univaq.sose.loanserviceprosumer.service.LoanNotFoundException;

import java.util.List;

/**
 * Interface defining the operations for managing loans.
 */
public interface LoanManager {

    /**
     * Opens a new loan based on the provided request.
     *
     * @param request the request containing loan details
     * @return the newly created loan
     */
    LoanDto openLoan(OpenLoanRequest request);

    /**
     * Retrieves all loans associated with a specific bank account ID.
     *
     * @param idBankAccount the ID of the bank account
     * @return the list of loans
     */
    List<LoanDto> getAllLoanByIdBankAccount(long idBankAccount);

    /**
     * Retrieves all loans associated with a specific account ID.
     *
     * @param idAccount the ID of the account
     * @return the list of loans
     */
    List<LoanDto> getAllLoanByIdAccount(long idAccount);

    /**
     * Closes the loan identified by the specified ID.
     *
     * @param idLoan the ID of the loan to be closed
     * @return the closed loan
     * @throws LoanNotFoundException if the loan is not found
     */
    LoanDto closeLoanByIdLoan(long idLoan) throws LoanNotFoundException;

    /**
     * Retrieves the loan identified by the specified ID.
     *
     * @param idLoan the ID of the loan
     * @return the loan
     * @throws LoanNotFoundException if the loan is not found
     */
    LoanDto getLoanById(long idLoan) throws LoanNotFoundException;
}
