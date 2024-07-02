package it.univaq.sose.bankaccountservice.business;

import it.univaq.sose.bankaccountservice.domain.dto.*;
import it.univaq.sose.bankaccountservice.webservice.BankAccountAlradyExistException;
import it.univaq.sose.bankaccountservice.webservice.InsufficientFundsException;
import it.univaq.sose.bankaccountservice.webservice.NotFoundException;

import java.util.List;

public interface BankAccountManager {
    BankAccountResponse createBankAccount(BankAccountRequest bankAccountRequest) throws BankAccountAlradyExistException;

    BankAccountResponse getBankAccountDetails(Long id) throws NotFoundException;

    TransactionResponse addMoney(BalanceUpdateRequest balanceUpdateRequest) throws NotFoundException;

    TransactionResponse removeMoney(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    TransactionResponse bancomatPay(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException;

    TransactionResponse executeTransaction(TransactionRequest transactionRequest) throws NotFoundException, InsufficientFundsException;

    List<TransactionResponse> getBankAccountTransactions(Long accountId);

    CheckBankAccountTransferResponse checkBankAccountTransfer(CheckBankAccountTransferRequest checkBankAccountTransferRequest) throws NotFoundException, InsufficientFundsException;
}
