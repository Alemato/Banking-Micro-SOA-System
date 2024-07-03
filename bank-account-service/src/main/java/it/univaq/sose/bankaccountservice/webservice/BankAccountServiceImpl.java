package it.univaq.sose.bankaccountservice.webservice;

import it.univaq.sose.bankaccountservice.business.BankAccountManager;
import it.univaq.sose.bankaccountservice.domain.dto.*;
import jakarta.jws.WebService;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.feature.Features;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Features(features = "org.apache.cxf.ext.logging.LoggingFeature")
@WebService(serviceName = "BankAccountService", portName = "BankAccountPort",
        targetNamespace = "http://service.ws.bankaccount/",
        endpointInterface = "it.univaq.sose.bankaccountservice.webservice.BankAccountService")
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountManager bankAccountManager;

    public BankAccountServiceImpl(BankAccountManager bankAccountManager) {
        this.bankAccountManager = bankAccountManager;
    }

    @Override
    public BankAccountResponse getBankAccountDetails(Long accountId) throws NotFoundException {
        return bankAccountManager.getBankAccountDetails(accountId);
    }

    @Override
    public BankAccountResponse createBankAccount(BankAccountRequest bankAccountRequest) {

        BankAccountResponse bankAccountResponse;
        try {
            bankAccountResponse = bankAccountManager.createBankAccount(bankAccountRequest);
        } catch (BankAccountAlradyExistException e) {
            throw new RuntimeException(e);
        }
        log.error("bankAccountResponse: {}", bankAccountResponse);
        return bankAccountResponse;
    }

    @Override
    public CheckBankAccountTransferResponse checkBankAccountTransfer(CheckBankAccountTransferRequest checkBankAccountTransferRequest) throws InsufficientFundsException, NotFoundException {
        return bankAccountManager.checkBankAccountTransfer(checkBankAccountTransferRequest);

    }

    @Override
    public TransactionResponse executeTransfer(TransactionRequest transactionRequest) throws InsufficientFundsException, NotFoundException {
        return bankAccountManager.executeTransaction(transactionRequest);
    }

    @Override
    public TransactionResponse addMoney(BalanceUpdateRequest balanceUpdateRequest) throws NotFoundException {
        return bankAccountManager.addMoney(balanceUpdateRequest);
    }

    @Override
    public TransactionResponse removeMoney(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException {
        return bankAccountManager.removeMoney(balanceUpdateRequest);
    }

    @Override
    public TransactionResponse bancomatPay(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException {
        return bankAccountManager.bancomatPay(balanceUpdateRequest);
    }

    @Override
    public List<TransactionResponse> getBankAccountTransactions(Long accountId) {
        return bankAccountManager.getBankAccountTransactions(accountId);
    }
}