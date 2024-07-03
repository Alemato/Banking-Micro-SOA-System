package it.univaq.sose.bancomatservice.business.impl;

import it.univaq.sose.bancomatservice.business.BancomatManager;
import it.univaq.sose.bancomatservice.domain.Bancomat;
import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import it.univaq.sose.bancomatservice.repository.BancomatRepository;
import it.univaq.sose.bancomatservice.repository.TransactionRepository;
import it.univaq.sose.bancomatservice.webservice.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Random;

@Service
public class BancomatManagerImpl implements BancomatManager {

    private final BancomatRepository bancomatRepository;
    private final TransactionRepository transactionRepository;
    private final Random random = new Random();

    public BancomatManagerImpl(BancomatRepository bancomatRepository, TransactionRepository transactionRepository) {
        this.bancomatRepository = bancomatRepository;
        this.transactionRepository = transactionRepository;

    }

    @Override
    public BancomatResponse getBancomatDetails(Long accountId) throws NotFoundException {
        Bancomat bancomat = bancomatRepository.findByAccountId(accountId).orElseThrow(() -> new NotFoundException("Bancomat with account ID: " + accountId + " not found."));
        return new BancomatResponse(bancomat.getId(), bancomat.getNumber(), bancomat.getCvv(), bancomat.getDataScadenza().toString(), bancomat.getAccountId());
    }

    @Override
    @Transactional
    public BancomatResponse createBancomat(Long accountId) {
        long numberPart1 = random.nextLong() % 100000000L;
        long numberPart2 = random.nextLong() % 100000000L;
        String numberWithoutSeparators = String.format("%08d%08d", numberPart1, numberPart2);
        String number = numberWithoutSeparators.replaceAll("(.{4})(?!$)", "$1-");

        String cvv = String.format("%03d", random.nextInt(1000));
        YearMonth dataScadenza = YearMonth.now().plusYears(3);

        Bancomat bancomat = new Bancomat();
        bancomat.setNumber(number);
        bancomat.setCvv(cvv);
        bancomat.setDataScadenza(dataScadenza);
        bancomat.setAccountId(accountId);

        bancomat = bancomatRepository.save(bancomat);

        return new BancomatResponse(bancomat.getId(), bancomat.getNumber(), bancomat.getCvv(), bancomat.getDataScadenza().toString(), bancomat.getAccountId());
    }
}
