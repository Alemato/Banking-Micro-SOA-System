package it.univaq.sose.bankaccountservice.webservice;

import it.univaq.sose.bankaccountservice.domain.dto.*;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@WebService(name = "BankAccountService")
public interface BankAccountService {

    @WebResult(name = "BankAccountResponse", targetNamespace = "")
    @WebMethod(action = "urn:GetBankAccountDetails")
    public BankAccountResponse getBankAccountDetails(@XmlElement(required = true) @WebParam(name = "accountId", targetNamespace = "") Long accountId) throws NotFoundException;

    @WebResult(name = "BankAccountResponse", targetNamespace = "")
    @WebMethod(action = "urn:CreateBankAccount")
    public BankAccountResponse createBankAccount(@XmlElement(required = true) @WebParam(name = "bankAccountRequest", targetNamespace = "") BankAccountRequest bankAccountRequest);

    @WebResult(name = "CheckBankAccountTransfer", targetNamespace = "")
    @WebMethod(action = "urn:CheckBankAccountTransfer")
    CheckBankAccountTransferResponse checkBankAccountTransfer(@XmlElement(required = true) @WebParam(name = "checkBankAccountTransferRequest", targetNamespace = "") CheckBankAccountTransferRequest checkBankAccountTransferRequest) throws InsufficientFundsException, NotFoundException;

    @WebResult(name = "ExecuteTransfer", targetNamespace = "")
    @WebMethod(action = "urn:ExecuteTransfer")
    TransactionResponse executeTransfer(@XmlElement(required = true) @WebParam(name = "transactionRequest", targetNamespace = "") TransactionRequest transactionRequest) throws InsufficientFundsException, NotFoundException;

    @WebResult(name = "AddMoney", targetNamespace = "")
    @WebMethod(action = "urn:AddMoney")
    TransactionResponse addMoney(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest", targetNamespace = "") BalanceUpdateRequest balanceUpdateRequest) throws NotFoundException;

    @WebResult(name = "RemoveMoney", targetNamespace = "")
    @WebMethod(action = "urn:RemoveMoney")
    TransactionResponse removeMoney(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest", targetNamespace = "") BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    @WebResult(name = "BancomatPay", targetNamespace = "")
    @WebMethod(action = "urn:BancomatPay")
    TransactionResponse bancomatPay(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest", targetNamespace = "") BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    @WebResult(name = "GetBankAccountTransactions", targetNamespace = "")
    @WebMethod(action = "urn:GetBankAccountTransactions")
    List<TransactionResponse> getBankAccountTransactions(@XmlElement(required = true) @WebParam(name = "accountId", targetNamespace = "") Long accountId);
}
