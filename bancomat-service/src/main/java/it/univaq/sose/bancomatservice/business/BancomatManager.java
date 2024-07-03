package it.univaq.sose.bancomatservice.business;

import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import it.univaq.sose.bancomatservice.domain.dto.TransactionRequest;
import it.univaq.sose.bancomatservice.domain.dto.TransactionResponse;
import it.univaq.sose.bancomatservice.webservice.BancomatAlradyExistingException;
import it.univaq.sose.bancomatservice.webservice.ExpiredBancomatException;
import it.univaq.sose.bancomatservice.webservice.NotFoundException;

import java.util.List;

public interface BancomatManager {
    BancomatResponse getBancomatDetails(Long accountId) throws NotFoundException;

    BancomatResponse createBancomat(Long accountId) throws BancomatAlradyExistingException;

    TransactionResponse executeTransaction(TransactionRequest transactionRequest) throws NotFoundException, ExpiredBancomatException;

    List<TransactionResponse> getBancomatTransactions(Long accountId);
}
