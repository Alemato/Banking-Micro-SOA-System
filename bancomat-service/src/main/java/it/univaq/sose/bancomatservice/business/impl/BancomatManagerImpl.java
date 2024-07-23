package it.univaq.sose.bancomatservice.business.impl;

import it.univaq.sose.bancomatservice.business.BancomatManager;
import it.univaq.sose.bancomatservice.domain.Bancomat;
import it.univaq.sose.bancomatservice.domain.Transaction;
import it.univaq.sose.bancomatservice.domain.dto.BancomatRequest;
import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import it.univaq.sose.bancomatservice.domain.dto.BancomatTransactionResponse;
import it.univaq.sose.bancomatservice.domain.dto.TransactionRequest;
import it.univaq.sose.bancomatservice.repository.BancomatRepository;
import it.univaq.sose.bancomatservice.repository.TransactionRepository;
import it.univaq.sose.bancomatservice.webservice.BancomatAlreadyExistingException;
import it.univaq.sose.bancomatservice.webservice.ExpiredBancomatException;
import it.univaq.sose.bancomatservice.webservice.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Implementation of the BancomatManager interface.
 */
@Service
public class BancomatManagerImpl implements BancomatManager {

    private final BancomatRepository bancomatRepository;
    private final TransactionRepository transactionRepository;
    private final Random random = new Random();

    public BancomatManagerImpl(BancomatRepository bancomatRepository, TransactionRepository transactionRepository) {
        this.bancomatRepository = bancomatRepository;
        this.transactionRepository = transactionRepository;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BancomatResponse getBancomatDetails(Long accountId) throws NotFoundException {
        Bancomat bancomat = bancomatRepository.findByAccountId(accountId).orElseThrow(() -> new NotFoundException("Bancomat with account ID: " + accountId + " not found."));
        return new BancomatResponse(bancomat.getId(), bancomat.getNumber(), bancomat.getCvv(), bancomat.getExpiryDate().toString(), bancomat.getAccountId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BancomatResponse getBancomatDetailsByNumber(String number) throws NotFoundException {
        Bancomat bancomat = bancomatRepository.findByNumber(number).orElseThrow(() -> new NotFoundException("Bancomat with number: " + number + " not found."));
        return new BancomatResponse(bancomat.getId(), bancomat.getNumber(), bancomat.getCvv(), bancomat.getExpiryDate().toString(), bancomat.getAccountId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public BancomatResponse createBancomat(BancomatRequest bancomatRequest) throws BancomatAlreadyExistingException {
        if (bancomatRepository.existsByAccountIdAndExpiryDateAfter(bancomatRequest.getAccountId(), YearMonth.now())) {
            throw new BancomatAlreadyExistingException("A non-expired Bancomat already exists");
        }

        // Generate a unique Bancomat number
        long numberPart1 = random.nextLong() % 100000000L;
        numberPart1 = numberPart1 < 0 ? -numberPart1 : numberPart1;
        long numberPart2 = random.nextLong() % 100000000L;
        numberPart2 = numberPart2 < 0 ? -numberPart2 : numberPart2;

        String numberWithoutSeparators = String.format("%08d%08d", numberPart1, numberPart2);
        String number = numberWithoutSeparators.replaceAll("(.{4})(?!$)", "$1-");

        // Generate a CVV and expiry date
        String cvv = String.format("%03d", random.nextInt(1000));
        YearMonth expiryDate = YearMonth.now().plusYears(3);

        // Create and save the Bancomat entity
        Bancomat bancomat = new Bancomat();
        bancomat.setNumber(number);
        bancomat.setCvv(cvv);
        bancomat.setExpiryDate(expiryDate);
        bancomat.setAccountId(bancomatRequest.getAccountId());
        bancomat.setBankAccountId(bancomatRequest.getBankAccountId());

        bancomat = bancomatRepository.save(bancomat);

        return new BancomatResponse(bancomat.getId(), bancomat.getNumber(), bancomat.getCvv(), bancomat.getExpiryDate().toString(), bancomat.getAccountId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public BancomatTransactionResponse executeTransaction(TransactionRequest transactionRequest) throws NotFoundException, ExpiredBancomatException {
        Bancomat bancomat = bancomatRepository.findByNumber(transactionRequest.getNumber())

                .orElseThrow(() -> new NotFoundException("Bancomat with number: " + transactionRequest.getNumber() + " not found."));

        // Check if the Bancomat is expired
        if (YearMonth.now().isAfter(bancomat.getExpiryDate())) {
            throw new ExpiredBancomatException("Bancomat expired");
        }

        // Create and save the transaction entity
        Transaction transaction = new Transaction();
        transaction.setBancomat(bancomat);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setTransactionCode(UUID.randomUUID().toString());

        transaction = transactionRepository.save(transaction);

        return new BancomatTransactionResponse(transaction.getId(), transaction.getTransactionCode(), transaction.getAmount(), transaction.getDescription(), transaction.getCreateDate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BancomatTransactionResponse> getBancomatTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.findDistinctByBancomat_AccountIdOrderByCreateDateDesc(accountId);
        return transactions.stream().map(t -> new BancomatTransactionResponse(t.getId(), t.getTransactionCode(), t.getAmount(), t.getDescription(), t.getCreateDate())).toList();
    }
}
