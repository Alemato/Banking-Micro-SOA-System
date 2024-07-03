package it.univaq.sose.bancomatservice.webservice;

import it.univaq.sose.bancomatservice.business.BancomatManager;
import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import it.univaq.sose.bancomatservice.domain.dto.TransactionRequest;
import it.univaq.sose.bancomatservice.domain.dto.TransactionResponse;
import jakarta.jws.WebService;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.feature.Features;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Features(features = "org.apache.cxf.ext.logging.LoggingFeature")
@WebService(serviceName = "BancomatService", portName = "BancomatPort",
        targetNamespace = "",
        endpointInterface = "it.univaq.sose.bancomatservice.webservice.BancomatService")
public class BancomatServiceImpl implements BancomatService {

    private final BancomatManager bancomatManager;

    public BancomatServiceImpl(BancomatManager bancomatManager) {
        this.bancomatManager = bancomatManager;
    }

    @Override
    public BancomatResponse getBancomatDetails(Long accountId) throws NotFoundException {
        return bancomatManager.getBancomatDetails(accountId);
    }

    @Override
    public BancomatResponse createBancomat(Long accountId) throws BancomatAlradyExistingException {
        return bancomatManager.createBancomat(accountId);
    }

    @Override
    public TransactionResponse executeTransaction(TransactionRequest transactionRequest) throws NotFoundException, ExpiredBancomatException {
        return bancomatManager.executeTransaction(transactionRequest);
    }

    @Override
    public List<TransactionResponse> getBancomatTransactions(Long accountId) {
        return bancomatManager.getBancomatTransactions(accountId);
    }
}
