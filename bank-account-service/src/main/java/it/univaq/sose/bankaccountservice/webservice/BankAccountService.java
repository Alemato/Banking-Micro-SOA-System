package it.univaq.sose.bankaccountservice.webservice;

import it.univaq.sose.bankaccountservice.domain.dto.*;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

/**
 * BankAccountService interface provides the web service operations for managing bank accounts.
 */
@WebService(name = "BankAccountService",
        targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
public interface BankAccountService {

    /**
     * Gets the details of a bank account by account ID.
     *
     * @param accountId the ID of the account
     * @return the BankAccountResponse containing the account details
     * @throws NotFoundException if the account is not found
     */
    @WebResult(name = "BankAccountResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBankAccountDetails")
    public BankAccountResponse getBankAccountDetails(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") Long accountId) throws NotFoundException;

    /**
     * Creates a new bank account.
     *
     * @param bankAccountRequest the request containing the details of the new bank account
     * @return the BankAccountResponse containing the created account details
     * @throws BankAccountAlradyExistException if the bank account already exists
     */
    @WebResult(name = "CreateBankAccountResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:CreateBankAccount")
    public BankAccountResponse createBankAccount(@XmlElement(required = true) @WebParam(name = "bankAccountRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") BankAccountRequest bankAccountRequest) throws BankAccountAlradyExistException;

    /**
     * Checks if a bank accounts is valid for transfer and retrieve the account IDs.
     *
     * @param checkBankAccountTransferRequest the request containing the transfer details
     * @return the CheckBankAccountTransferResponse containing the result of the check
     * @throws InsufficientFundsException if there are insufficient funds for the transfer
     * @throws NotFoundException          if the account is not found
     */
    @WebResult(name = "CheckBankAccountTransferResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:CheckBankAccountTransfer")
    CheckBankAccountTransferResponse checkBankAccountTransfer(@XmlElement(required = true) @WebParam(name = "checkBankAccountTransferRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") CheckBankAccountTransferRequest checkBankAccountTransferRequest) throws InsufficientFundsException, NotFoundException;

    /**
     * Executes a bank account transfer.
     *
     * @param transferRequest the request containing the transfer details
     * @return the TransactionResponse containing the details of the executed transfer
     * @throws InsufficientFundsException if there are insufficient funds for the transfer
     * @throws NotFoundException if the account is not found
     */
    @WebResult(name = "ExecuteTransferResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:ExecuteTransfer")
    TransactionResponse executeTransfer(@XmlElement(required = true) @WebParam(name = "transactionRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") TransferRequest transferRequest) throws InsufficientFundsException, NotFoundException;

    /**
     * Adds money to a bank account.
     *
     * @param balanceUpdateRequest the request containing the amount to add
     * @return the TransactionResponse containing the details of the transaction
     * @throws NotFoundException if the account is not found
     */
    @WebResult(name = "AddMoneyResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:AddMoney")
    TransactionResponse addMoney(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") BalanceUpdateRequest balanceUpdateRequest) throws NotFoundException;

    /**
     * Removes money from a bank account.
     *
     * @param balanceUpdateRequest the request containing the amount to remove
     * @return the TransactionResponse containing the details of the transaction
     * @throws InsufficientFundsException if there are insufficient funds in the account
     * @throws NotFoundException if the account is not found
     */
    @WebResult(name = "RemoveMoneyResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:RemoveMoney")
    TransactionResponse removeMoney(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    /**
     * Processes a payment using a Bancomat card.
     *
     * @param balanceUpdateRequest the request containing the payment details
     * @return the TransactionResponse containing the details of the transaction
     * @throws InsufficientFundsException if there are insufficient funds in the account
     * @throws NotFoundException if the account is not found
     */
    @WebResult(name = "BancomatPayResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:BancomatPay")
    TransactionResponse bancomatPay(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    /**
     * Gets the transactions of a bank account by account ID.
     *
     * @param accountId the ID of the account
     * @return a list of TransactionResponse containing the account transactions
     */
    @WebResult(name = "GetBankAccountTransactionsResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBankAccountTransactions")
    List<TransactionResponse> getBankAccountTransactions(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") Long accountId);
}
