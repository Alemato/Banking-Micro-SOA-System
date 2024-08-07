package it.univaq.sose.bankaccountservice.business.impl;

import it.univaq.sose.bankaccountservice.business.BankAccountManager;
import it.univaq.sose.bankaccountservice.domain.BankAccount;
import it.univaq.sose.bankaccountservice.domain.Transaction;
import it.univaq.sose.bankaccountservice.domain.TransactionType;
import it.univaq.sose.bankaccountservice.domain.dto.*;
import it.univaq.sose.bankaccountservice.repository.BankAccountRepository;
import it.univaq.sose.bankaccountservice.repository.TransactionRepository;
import it.univaq.sose.bankaccountservice.webservice.BankAccountAlradyExistException;
import it.univaq.sose.bankaccountservice.webservice.InsufficientFundsException;
import it.univaq.sose.bankaccountservice.webservice.NotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of the BankAccountManager interface.
 */
@Service
public class BankAccountManagerImpl implements BankAccountManager {

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;

    private final JdbcTemplate jdbcTemplate;

    public BankAccountManagerImpl(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository, JdbcTemplate jdbcTemplate) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public BankAccountResponse createBankAccount(BankAccountRequest bankAccountRequest) throws BankAccountAlradyExistException {
        if (bankAccountRepository.existsByAccountId(bankAccountRequest.getAccountId())) {
            throw new BankAccountAlradyExistException("A Bank Account already exists for account with ID: " + bankAccountRequest.getAccountId());
        }
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountId(bankAccountRequest.getAccountId());
        bankAccount.setIban(generateNewIban());
        bankAccount.setBalance(bankAccountRequest.getBalance());
        bankAccount = bankAccountRepository.save(bankAccount);

        // Save an initial deposit transaction if the initial balance is greater than zero
        if (bankAccountRequest.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            saveTransaction(null, bankAccount, bankAccountRequest.getBalance(), TransactionType.DEPOSIT, "Bank account opening.");
        }
        return new BankAccountResponse(bankAccount.getId(), bankAccount.getAccountId(), bankAccount.getIban(), bankAccount.getBalance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccountResponse getBankAccountDetails(Long idAccount) throws NotFoundException {
        BankAccount bankAccount = bankAccountRepository.findByAccountId(idAccount).orElseThrow(() -> new NotFoundException("Bank Account with ID: " + idAccount + " not found."));
        return new BankAccountResponse(bankAccount.getId(), bankAccount.getAccountId(), bankAccount.getIban(), bankAccount.getBalance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TransactionResponse addMoney(BalanceUpdateRequest balanceUpdateRequest) throws NotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(balanceUpdateRequest.getBankAccountId()).orElseThrow(() -> new NotFoundException("Bank Account with ID: " + balanceUpdateRequest.getBankAccountId() + " not found."));
        bankAccount.setBalance(bankAccount.getBalance().add(balanceUpdateRequest.getAmount()));
        Transaction transaction = saveTransaction(null, bankAccount, balanceUpdateRequest.getAmount(), TransactionType.DEPOSIT, balanceUpdateRequest.getDescription());
        bankAccount = bankAccountRepository.save(bankAccount);
        BankAccountResponse receiverBankAccountResponse = new BankAccountResponse(bankAccount.getId(), bankAccount.getAccountId(), bankAccount.getIban(), bankAccount.getBalance());
        return new TransactionResponse(transaction.getId(), transaction.getTransactionCode(), transaction.getAmount(), transaction.getDescription(), transaction.getTransactionType(), transaction.getCreateDate(), null, receiverBankAccountResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TransactionResponse removeMoney(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(balanceUpdateRequest.getBankAccountId()).orElseThrow(() -> new NotFoundException("Bank Account with ID: " + balanceUpdateRequest.getBankAccountId() + " not found."));
        if (bankAccount.getBalance().compareTo(balanceUpdateRequest.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        bankAccount.setBalance(bankAccount.getBalance().subtract(balanceUpdateRequest.getAmount()));
        Transaction transaction = saveTransaction(bankAccount, null, balanceUpdateRequest.getAmount(), TransactionType.WITHDRAWAL, balanceUpdateRequest.getDescription());
        bankAccount = bankAccountRepository.save(bankAccount);

        BankAccountResponse senderBankAccountResponse = new BankAccountResponse(bankAccount.getId(), bankAccount.getAccountId(), bankAccount.getIban(), bankAccount.getBalance());
        return new TransactionResponse(transaction.getId(), transaction.getTransactionCode(), transaction.getAmount().negate(), transaction.getDescription(), transaction.getTransactionType(), transaction.getCreateDate(), senderBankAccountResponse, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TransactionResponse bancomatPay(BalanceUpdateRequest balanceUpdateRequest) throws InsufficientFundsException, NotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(balanceUpdateRequest.getBankAccountId()).orElseThrow(() -> new NotFoundException("Bank Account with ID: " + balanceUpdateRequest.getBankAccountId() + " not found."));
        if (bankAccount.getBalance().compareTo(balanceUpdateRequest.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        bankAccount.setBalance(bankAccount.getBalance().subtract(balanceUpdateRequest.getAmount()));
        Transaction transaction = saveTransaction(bankAccount, null, balanceUpdateRequest.getAmount(), TransactionType.BANCOMAT, balanceUpdateRequest.getDescription());
        bankAccount = bankAccountRepository.save(bankAccount);

        BankAccountResponse senderBankAccountResponse = new BankAccountResponse(bankAccount.getId(), bankAccount.getAccountId(), bankAccount.getIban(), bankAccount.getBalance());
        return new TransactionResponse(transaction.getId(), transaction.getTransactionCode(), transaction.getAmount().negate(), transaction.getDescription(), transaction.getTransactionType(), transaction.getCreateDate(), senderBankAccountResponse, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TransactionResponse executeTransfer(TransferRequest transferRequest) throws NotFoundException, InsufficientFundsException {
        BankAccount senderBankAccount = bankAccountRepository.findByAccountId(transferRequest.getSenderAccountId())
                .orElseThrow(() -> new NotFoundException("Bank Account with Account ID: " + transferRequest.getSenderAccountId() + " not found."));

        BigDecimal amount = transferRequest.getAmount();
        if (senderBankAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        BankAccount receiverBankAccount = bankAccountRepository.findByAccountId(transferRequest.getReceiverAccountId())
                .orElseThrow(() -> new NotFoundException("Bank Account with Account ID: " + transferRequest.getReceiverAccountId() + " not found."));

        // Subtract the amount from the sender's balance
        senderBankAccount.setBalance(senderBankAccount.getBalance().subtract(amount));
        // Add the amount to the receiver's balance
        receiverBankAccount.setBalance(receiverBankAccount.getBalance().add(amount));

        bankAccountRepository.save(senderBankAccount);
        bankAccountRepository.save(receiverBankAccount);

        // Save the transfer transaction
        Transaction transaction = saveTransaction(senderBankAccount, receiverBankAccount, transferRequest.getAmount(), TransactionType.TRANSFER, transferRequest.getDescription());
        BankAccountResponse senderBankAccountResponse = new BankAccountResponse(senderBankAccount.getId(), senderBankAccount.getAccountId(), senderBankAccount.getIban(), senderBankAccount.getBalance());
        BankAccountResponse receiverBankAccountResponse = new BankAccountResponse(receiverBankAccount.getId(), receiverBankAccount.getAccountId(), receiverBankAccount.getIban(), receiverBankAccount.getBalance());
        return new TransactionResponse(transaction.getId(), transaction.getTransactionCode(), transaction.getAmount().negate(), transaction.getDescription(), transaction.getTransactionType(), transaction.getCreateDate(), senderBankAccountResponse, receiverBankAccountResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransactionResponse> getBankAccountTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.findDistinctBySenderBankAccount_AccountIdOrReceiverBankAccount_AccountIdOrderByCreateDateAsc(accountId, accountId);
        List<TransactionResponse> transactionsResponse = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionsResponse.add(mapToTransactionResponse(transaction, accountId));
        }
        return transactionsResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckBankAccountTransferResponse checkBankAccountTransfer(CheckBankAccountTransferRequest checkBankAccountTransferRequest) throws NotFoundException, InsufficientFundsException {
        BankAccount bankAccountSender = bankAccountRepository.findByIban(checkBankAccountTransferRequest.getSenderIban())
                .orElseThrow(() -> new NotFoundException("Bank Account with Iban " + checkBankAccountTransferRequest.getSenderIban() + " not found."));
        if (bankAccountSender.getBalance().compareTo(checkBankAccountTransferRequest.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        BankAccount bankAccountReceiver = bankAccountRepository.findByIban(checkBankAccountTransferRequest.getReceiverIban())
                .orElseThrow(() -> new NotFoundException("Bank Account with Iban " + checkBankAccountTransferRequest.getReceiverIban() + " not found."));
        return new CheckBankAccountTransferResponse(bankAccountSender.getAccountId(), bankAccountReceiver.getAccountId());
    }

    /**
     * Saves a transaction to the database.
     *
     * @param senderAccount   the sender bank account
     * @param receiverAccount the receiver bank account
     * @param amount          the amount of the transaction
     * @param type            the type of the transaction
     * @param description     the description of the transaction
     * @return the saved transaction
     */
    public Transaction saveTransaction(BankAccount senderAccount, BankAccount receiverAccount, BigDecimal amount, TransactionType type, String description) {
        Transaction transaction = new Transaction();
        transaction.setSenderBankAccount(senderAccount);
        transaction.setReceiverBankAccount(receiverAccount);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setDescription(description);
        transaction.setTransactionCode(UUID.randomUUID().toString());
        return transactionRepository.save(transaction);
    }


    /**
     * Generates a new IBAN for a bank account.
     *
     * @return the generated IBAN
     */
    private String generateNewIban() {
        Long sequenceValue = jdbcTemplate.queryForObject("SELECT nextval('iban_sequence')", Long.class);

        String countryCode = "IT";
        String checkDigits = calculateCheckDigits("IT00" + sequenceValue);
        String bankCode = "A";
        String accountNumber = String.format("%020d", sequenceValue);

        return countryCode + checkDigits + bankCode + accountNumber;
    }

    /**
     * Calculates the check digits for an IBAN.
     *
     * @param iban the IBAN to calculate the check digits for
     * @return the calculated check digits
     */

    private String calculateCheckDigits(String iban) {
        String reformattedIban = iban.substring(4) + iban.substring(0, 4);
        StringBuilder numericIban = new StringBuilder();
        for (char ch : reformattedIban.toCharArray()) {
            int numericValue = Character.getNumericValue(ch);
            numericIban.append(numericValue);
        }
        BigInteger ibanInt = new BigInteger(numericIban.toString());
        BigInteger mod97 = ibanInt.mod(BigInteger.valueOf(97));
        int checkDigits = 98 - mod97.intValue();
        return String.format("%02d", checkDigits);
    }

    /**
     * Maps a Transaction entity to a TransactionResponse DTO.
     *
     * @param transaction the transaction entity
     * @param accountId   the account ID of the requester
     * @return the mapped transaction response
     */
    private TransactionResponse mapToTransactionResponse(Transaction transaction, Long accountId) {
        BankAccountResponse senderResponse = transaction.getSenderBankAccount() != null ?
                new BankAccountResponse(
                        transaction.getSenderBankAccount().getId(),
                        transaction.getSenderBankAccount().getAccountId(),
                        transaction.getSenderBankAccount().getIban(),
                        transaction.getSenderBankAccount().getBalance()
                ) : null;

        BankAccountResponse receiverResponse = transaction.getReceiverBankAccount() != null ?
                new BankAccountResponse(
                        transaction.getReceiverBankAccount().getId(),
                        transaction.getReceiverBankAccount().getAccountId(),
                        transaction.getReceiverBankAccount().getIban(),
                        transaction.getReceiverBankAccount().getBalance()
                ) : null;

        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionCode(),
                Objects.equals(accountId, senderResponse != null ? senderResponse.getAccountId() : null) ? transaction.getAmount().negate() : transaction.getAmount(),
                transaction.getDescription(),
                transaction.getTransactionType(),
                transaction.getCreateDate(),
                senderResponse,
                receiverResponse
        );
    }
}
