package it.univaq.sose.bancomatservice.webservice;

import it.univaq.sose.bancomatservice.business.BancomatManager;
import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import jakarta.jws.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
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
    public BancomatResponse createBancomat(Long accountId) {
        return bancomatManager.createBancomat(accountId);
    }
}
