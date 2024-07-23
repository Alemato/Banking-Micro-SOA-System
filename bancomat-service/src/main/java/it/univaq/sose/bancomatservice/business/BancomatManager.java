package it.univaq.sose.bancomatservice.business;

import it.univaq.sose.bancomatservice.domain.dto.BancomatRequest;
import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import it.univaq.sose.bancomatservice.domain.dto.BancomatTransactionResponse;
import it.univaq.sose.bancomatservice.domain.dto.TransactionRequest;
import it.univaq.sose.bancomatservice.webservice.BancomatAlreadyExistingException;
import it.univaq.sose.bancomatservice.webservice.ExpiredBancomatException;
import it.univaq.sose.bancomatservice.webservice.NotFoundException;

import java.util.List;

/**
 * Interface for managing Bancomat operations.
 */
public interface BancomatManager {
    /**
     * Retrieves the Bancomat details associated with a given account ID.
     *
     * @param accountId the account ID
     * @return the Bancomat details
     * @throws NotFoundException if no Bancomat is found for the given account ID
     */
    BancomatResponse getBancomatDetails(Long accountId) throws NotFoundException;

    /**
     * Retrieves the Bancomat details associated with a given Bancomat number.
     *
     * @param number the Bancomat number
     * @return the Bancomat details
     * @throws NotFoundException if no Bancomat is found for the given number
     */
    BancomatResponse getBancomatDetailsByNumber(String number) throws NotFoundException;

    /**
     * Creates a new Bancomat.
     *
     * @param bancomatRequest the Bancomat request data
     * @return the created Bancomat details
     * @throws BancomatAlreadyExistingException if a non-expired Bancomat already exists for the account
     */
    BancomatResponse createBancomat(BancomatRequest bancomatRequest) throws BancomatAlreadyExistingException;

    /**
     * Executes a transaction using the provided transaction request data.
     *
     * @param transactionRequest the transaction request data
     * @return the transaction response details
     * @throws NotFoundException        if no Bancomat is found for the given number
     * @throws ExpiredBancomatException if the Bancomat is expired
     */
    BancomatTransactionResponse executeTransaction(TransactionRequest transactionRequest) throws NotFoundException, ExpiredBancomatException;

    /**
     * Retrieves a list of transactions associated with a given account ID.
     *
     * @param accountId the account ID
     * @return the list of transactions
     */
    List<BancomatTransactionResponse> getBancomatTransactions(Long accountId);
}
