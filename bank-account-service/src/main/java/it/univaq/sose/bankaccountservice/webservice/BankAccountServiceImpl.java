package it.univaq.sose.bankaccountservice.webservice;

import it.univaq.sose.bankaccountservice.business.BankAccountManager;
import it.univaq.sose.bankaccountservice.domain.dto.*;
import jakarta.jws.WebService;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.feature.Features;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the BankAccountService interface.
 * This class provides the web service operations for managing bank accounts.
 */
@Slf4j
@Service
@Features(features = "org.apache.cxf.ext.logging.LoggingFeature")
@WebService(serviceName = "BankAccountService", portName = "BankAccountPort",
        targetNamespace = "http://webservice.bankaccountservice.sose.univaq.it/",
        endpointInterface = "it.univaq.sose.bankaccountservice.webservice.BankAccountService")
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountManager bankAccountManager;

    public BankAccountServiceImpl(BankAccountManager bankAccountManager) {
        this.bankAccountManager = bankAccountManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccountResponse getBankAccountDetails(Long accountId) throws NotFoundException {
        return bankAccountManager.getBankAccountDetails(accountId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccountResponse createBankAccount(BankAccountRequest bankAccountRequest) throws BankAccountAlradyExistException {
        return bankAccountManager.createBankAccount(bankAccountRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckBankAccountTransferResponse checkBankAccountTransfer(CheckBankAccountTransferRequest checkBankAccountTransferRequest) throws InsufficientFundsException, NotFoundException {
        return bankAccountManager.checkBankAccountTransfer(checkBankAccountTransferRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransactionResponse executeTransfer(TransferRequest transferRequest) throws InsufficientFundsException, NotFoundException {
        return bankAccountManager.executeTransfer(transferRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransactionResponse addMoney(BalanceUpdateRequest balanceUpdateRequest) throws NotFoundException {
        return bankAccountManager.addMoney(balanceUpdateRequest);
    }

    @Override
    public TransactionResponse removeMoney(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException {
        return bankAccountManager.removeMoney(balanceUpdateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransactionResponse bancomatPay(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException {
        return bankAccountManager.bancomatPay(balanceUpdateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransactionResponse> getBankAccountTransactions(Long accountId) {
        return bankAccountManager.getBankAccountTransactions(accountId);
    }
}