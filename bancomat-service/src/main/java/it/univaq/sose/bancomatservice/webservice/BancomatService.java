package it.univaq.sose.bancomatservice.webservice;

import it.univaq.sose.bancomatservice.domain.dto.*;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.ws.AsyncHandler;
import jakarta.xml.ws.ResponseWrapper;

import java.util.List;
import java.util.concurrent.Future;

@WebService(name = "BancomatService")
public interface BancomatService {

    @WebResult(name = "GetBancomatDetailsResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatDetails")
    @ResponseWrapper(localName = "getBancomatDetailsResponse",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatDetailsResponse")
    public BancomatResponse getBancomatDetails(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId) throws NotFoundException, BancomatException;

    @WebResult(name = "GetBancomatDetailsAsync",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatDetailsAsync")
    @ResponseWrapper(localName = "getBancomatDetailsResponse",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatDetailsResponse")
    public Future<?> getBancomatDetailsAsync(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId, AsyncHandler<GetBancomatDetailsResponse> asyncHandler) throws NotFoundException, BancomatException;

    @WebResult(name = "GetBancomatDetailsByNumberResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatDetailsByNumber")
    public BancomatResponse getBancomatDetailsByNumber(@XmlElement(required = true) @WebParam(name = "number",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") String number) throws NotFoundException;

    @WebResult(name = "CreateBancomatResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:CreateBancomat")
    public BancomatResponse createBancomat(@XmlElement(required = true) @WebParam(name = "bancomatRequest",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") BancomatRequest bancomatRequest) throws NotFoundException, BancomatAlreadyExistingException;

    @WebResult(name = "ExecuteTransactionResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:ExecuteTransaction")
    public BancomatTransactionResponse executeTransaction(@XmlElement(required = true) @WebParam(name = "transactionRequest",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") TransactionRequest transactionRequest) throws NotFoundException, ExpiredBancomatException;

    @WebResult(name = "GetBancomatTransactionsResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatTransactions")
    @ResponseWrapper(localName = "getBancomatTransactionsResponse",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatTransactionsResponse")
    public List<BancomatTransactionResponse> getBancomatTransactions(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId);

    @WebResult(name = "GetBancomatTransactionsResponseAsync",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatTransactionsAsync")
    @ResponseWrapper(localName = "getBancomatTransactionsResponse",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatTransactionsResponse")
    public Future<?> getBancomatTransactionsAsync(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId, AsyncHandler<GetBancomatTransactionsResponse> asyncHandler) throws BancomatException;
}
