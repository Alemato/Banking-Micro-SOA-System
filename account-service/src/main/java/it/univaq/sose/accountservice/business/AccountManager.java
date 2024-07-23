package it.univaq.sose.accountservice.business;

import it.univaq.sose.accountservice.domain.dto.AccountResponse;
import it.univaq.sose.accountservice.domain.dto.AddIdBankAccountRequest;
import it.univaq.sose.accountservice.domain.dto.OpenBankAccountRequest;
import it.univaq.sose.accountservice.domain.dto.UserCredentials;
import it.univaq.sose.accountservice.security.AuthenticationException;
import it.univaq.sose.accountservice.service.NotFoundException;

/**
 * Interface for managing account operations.
 */
public interface AccountManager {

    /**
     * Generates a JWT token for the given user credentials.
     *
     * @param userCredentials the user credentials
     * @return the JWT token
     * @throws AuthenticationException if authentication fails
     */
    String getJwtToken(UserCredentials userCredentials) throws AuthenticationException;

    /**
     * Checks the validity of a given JWT token.
     *
     * @param token the JWT token
     * @return true if the token is valid, false otherwise
     */
    Boolean checkJwtToken(String token);

    /**
     * Creates a customer account.
     *
     * @param openBankAccountRequest the request data for opening a bank account
     * @return the response containing account details
     */
    AccountResponse createAccountCustomer(OpenBankAccountRequest openBankAccountRequest);

    /**
     * Updates an account with the ID of a bank account.
     *
     * @param id      the account ID
     * @param request the request data containing the bank account ID
     * @return the response containing updated account details
     * @throws NotFoundException if the account is not found
     */
    AccountResponse updateAccountWithIdBankAccount(long id, AddIdBankAccountRequest request) throws NotFoundException;

    /**
     * Creates a banker account.
     *
     * @param request the request data for opening a bank account
     * @return the response containing account details
     */
    AccountResponse createAccountBanker(OpenBankAccountRequest request);

    /**
     * Creates an admin account.
     *
     * @param request the request data for opening a bank account
     * @return the response containing account details
     */
    AccountResponse createAccountAdmin(OpenBankAccountRequest request);

    /**
     * Retrieves an account by its ID.
     *
     * @param idAccount the account ID
     * @return the response containing account details
     * @throws NotFoundException if the account is not found
     */
    AccountResponse getAccountByIdAccount(Long idAccount) throws NotFoundException;
}
