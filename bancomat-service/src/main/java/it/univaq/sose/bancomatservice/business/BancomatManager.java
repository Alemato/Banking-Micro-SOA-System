package it.univaq.sose.bancomatservice.business;

import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import it.univaq.sose.bancomatservice.webservice.NotFoundException;

public interface BancomatManager {
    BancomatResponse getBancomatDetails(Long accountId) throws NotFoundException;

    BancomatResponse createBancomat(Long accountId);
}
