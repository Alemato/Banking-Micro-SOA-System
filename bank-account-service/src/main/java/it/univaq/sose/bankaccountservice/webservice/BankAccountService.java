package it.univaq.sose.bankaccountservice.webservice;

import it.univaq.sose.bankaccountservice.domain.dto.*;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@WebService(name = "BankAccountService",
        targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
public interface BankAccountService {

    @WebResult(name = "BankAccountResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBankAccountDetails")
    public BankAccountResponse getBankAccountDetails(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") Long accountId) throws NotFoundException;

    @WebResult(name = "CreateBankAccountResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:CreateBankAccount")
    public BankAccountResponse createBankAccount(@XmlElement(required = true) @WebParam(name = "bankAccountRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") BankAccountRequest bankAccountRequest) throws BankAccountAlradyExistException;

    @WebResult(name = "CheckBankAccountTransferResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:CheckBankAccountTransfer")
    CheckBankAccountTransferResponse checkBankAccountTransfer(@XmlElement(required = true) @WebParam(name = "checkBankAccountTransferRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") CheckBankAccountTransferRequest checkBankAccountTransferRequest) throws InsufficientFundsException, NotFoundException;

    @WebResult(name = "ExecuteTransferResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:ExecuteTransfer")
    TransactionResponse executeTransfer(@XmlElement(required = true) @WebParam(name = "transactionRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") TransactionRequest transactionRequest) throws InsufficientFundsException, NotFoundException;

    @WebResult(name = "AddMoneyResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:AddMoney")
    TransactionResponse addMoney(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") BalanceUpdateRequest balanceUpdateRequest) throws NotFoundException;

    @WebResult(name = "RemoveMoneyResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:RemoveMoney")
    TransactionResponse removeMoney(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    @WebResult(name = "BancomatPayResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:BancomatPay")
    TransactionResponse bancomatPay(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    @WebResult(name = "GetBankAccountTransactionsResponse",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBankAccountTransactions")
    List<TransactionResponse> getBankAccountTransactions(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/") Long accountId);
}
