package it.univaq.sose.bankaccountservice.business;

import it.univaq.sose.bankaccountservice.domain.dto.*;
import it.univaq.sose.bankaccountservice.webservice.BankAccountAlradyExistException;
import it.univaq.sose.bankaccountservice.webservice.InsufficientFundsException;
import it.univaq.sose.bankaccountservice.webservice.NotFoundException;

import java.util.List;

/**
 * Interface for managing bank accounts and transactions.
 */
public interface BankAccountManager {
    /**
     * Creates a new bank account.
     *
     * @param bankAccountRequest the request to create a new bank account
     * @return the response containing the details of the created bank account
     * @throws BankAccountAlradyExistException if a bank account already exists for the given account ID
     */
    BankAccountResponse createBankAccount(BankAccountRequest bankAccountRequest) throws BankAccountAlradyExistException;

    /**
     * Retrieves the details of a bank account by its account ID.
     *
     * @param idAccount the ID of the account
     * @return the response containing the bank account details
     * @throws NotFoundException if the bank account is not found
     */
    BankAccountResponse getBankAccountDetails(Long idAccount) throws NotFoundException;

    /**
     * Adds money to a bank account.
     *
     * @param balanceUpdateRequest the request to add money
     * @return the response containing the transaction details
     * @throws NotFoundException if the bank account is not found
     */
    TransactionResponse addMoney(BalanceUpdateRequest balanceUpdateRequest) throws NotFoundException;

    /**
     * Removes money from a bank account.
     *
     * @param balanceUpdateRequest the request to remove money
     * @return the response containing the transaction details
     * @throws InsufficientFundsException if there are insufficient funds in the account
     * @throws NotFoundException          if the bank account is not found
     */
    TransactionResponse removeMoney(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    /**
     * Processes a Bancomat payment.
     *
     * @param balanceUpdateRequest the request to process the payment
     * @return the response containing the transaction details
     * @throws InsufficientFundsException if there are insufficient funds in the account
     * @throws NotFoundException if the bank account is not found
     */
    TransactionResponse bancomatPay(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    /**
     * Executes a transfer between two bank accounts.
     *
     * @param transferRequest the request to execute the transfer
     * @return the response containing the transaction details
     * @throws NotFoundException if any of the bank accounts are not found
     * @throws InsufficientFundsException if there are insufficient funds in the sender's account
     */
    TransactionResponse executeTransfer(TransferRequest transferRequest) throws NotFoundException, InsufficientFundsException;

    /**
     * Retrieves the transactions of a bank account.
     *
     * @param accountId the ID of the account
     * @return the list of transaction responses
     */
    List<TransactionResponse> getBankAccountTransactions(Long accountId);

    /**
     * Checks if a transfer can be made between two bank accounts.
     *
     * @param checkBankAccountTransferRequest the request to check the transfer
     * @return the response containing the check result
     * @throws NotFoundException if any of the bank accounts are not found
     * @throws InsufficientFundsException if there are insufficient funds in the sender's account
     */
    CheckBankAccountTransferResponse checkBankAccountTransfer(CheckBankAccountTransferRequest checkBankAccountTransferRequest) throws NotFoundException, InsufficientFundsException;
}
