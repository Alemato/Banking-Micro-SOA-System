package it.univaq.sose.bancomatservice.webservice;

import it.univaq.sose.bancomatservice.business.BancomatManager;
import it.univaq.sose.bancomatservice.domain.dto.*;
import jakarta.jws.WebService;
import jakarta.xml.ws.AsyncHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.annotations.UseAsyncMethod;
import org.apache.cxf.feature.Features;
import org.apache.cxf.jaxws.ServerAsyncResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Service
@Features(features = "org.apache.cxf.ext.logging.LoggingFeature")
@WebService(serviceName = "BancomatService", portName = "BancomatPort",
        targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/",
        endpointInterface = "it.univaq.sose.bancomatservice.webservice.BancomatService")
public class BancomatServiceImpl implements BancomatService {

    private final BancomatManager bancomatManager;

    public BancomatServiceImpl(BancomatManager bancomatManager) {
        this.bancomatManager = bancomatManager;
    }

    @Override
    @UseAsyncMethod
    public BancomatResponse getBancomatDetails(Long accountId) throws NotFoundException, BancomatException {
        return bancomatManager.getBancomatDetails(accountId);
    }

    @Override
    public Future<?> getBancomatDetailsAsync(Long accountId, AsyncHandler<GetBancomatDetailsResponse> asyncHandler) throws NotFoundException, BancomatException {
        final ServerAsyncResponse<GetBancomatDetailsResponse> r
                = new ServerAsyncResponse<>();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                BancomatResponse bancomatResponse = bancomatManager.getBancomatDetails(accountId);
                GetBancomatDetailsResponse response = new GetBancomatDetailsResponse();
                response.setGetBancomatDetailsResponse(bancomatResponse);
                r.set(response);
                asyncHandler.handleResponse(r);
            } catch (InterruptedException e) {
                r.exception(new BancomatException(e.getMessage()));
                asyncHandler.handleResponse(r);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            } catch (NotFoundException e) {
                r.exception(e);
                asyncHandler.handleResponse(r);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
        return r;
    }

    @Override
    public BancomatResponse getBancomatDetailsByNumber(String number) throws NotFoundException {
        return bancomatManager.getBancomatDetailsByNumber(number);
    }

    @Override
    public BancomatResponse createBancomat(BancomatRequest bancomatRequest) throws BancomatAlreadyExistingException {
        return bancomatManager.createBancomat(bancomatRequest);
    }

    @Override
    public BancomatTransactionResponse executeTransaction(TransactionRequest transactionRequest) throws NotFoundException, ExpiredBancomatException {
        return bancomatManager.executeTransaction(transactionRequest);
    }

    @Override
    @UseAsyncMethod
    public List<BancomatTransactionResponse> getBancomatTransactions(Long accountId) {
        List<BancomatTransactionResponse> response = bancomatManager.getBancomatTransactions(accountId);
        return response;
    }

    @Override
    public Future<?> getBancomatTransactionsAsync(Long accountId, AsyncHandler<GetBancomatTransactionsResponse> asyncHandler) throws BancomatException {
        final ServerAsyncResponse<GetBancomatTransactionsResponse> r
                = new ServerAsyncResponse<>();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                List<BancomatTransactionResponse> bancomatTransactionResponseList = bancomatManager.getBancomatTransactions(accountId);
                GetBancomatTransactionsResponse response = new GetBancomatTransactionsResponse();
                response.getGetBancomatTransactionsResponse().addAll(bancomatTransactionResponseList);
                r.set(response);
                asyncHandler.handleResponse(r);
            } catch (InterruptedException e) {
                r.exception(new BancomatException(e.getMessage()));
                asyncHandler.handleResponse(r);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
        return r;
    }
}
