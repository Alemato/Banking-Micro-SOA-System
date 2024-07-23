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

/**
 * Web Service interface for Bancomat operations.
 */
@WebService(name = "BancomatService")
public interface BancomatService {

    /**
     * Retrieves the details of a Bancomat by account ID.
     *
     * @param accountId the account ID
     * @return the Bancomat details
     * @throws NotFoundException if the Bancomat is not found
     * @throws BancomatException if a general Bancomat error occurs
     */
    @WebResult(name = "GetBancomatDetailsResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatDetails")
    @ResponseWrapper(localName = "getBancomatDetailsResponse",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatDetailsResponse")
    public BancomatResponse getBancomatDetails(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId) throws NotFoundException, BancomatException;

    /**
     * Asynchronously retrieves the details of a Bancomat by account ID.
     *
     * @param accountId    the account ID
     * @param asyncHandler the async handler
     * @return a Future representing the async operation
     * @throws NotFoundException if the Bancomat is not found
     * @throws BancomatException if a general Bancomat error occurs
     */
    @WebResult(name = "GetBancomatDetailsAsync",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatDetailsAsync")
    @ResponseWrapper(localName = "getBancomatDetailsResponse",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatDetailsResponse")
    public Future<?> getBancomatDetailsAsync(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId, AsyncHandler<GetBancomatDetailsResponse> asyncHandler) throws NotFoundException, BancomatException;

    /**
     * Retrieves the details of a Bancomat by number.
     *
     * @param number the Bancomat number
     * @return the Bancomat details
     * @throws NotFoundException if the Bancomat is not found
     */
    @WebResult(name = "GetBancomatDetailsByNumberResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatDetailsByNumber")
    public BancomatResponse getBancomatDetailsByNumber(@XmlElement(required = true) @WebParam(name = "number",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") String number) throws NotFoundException;

    /**
     * Creates a new Bancomat.
     *
     * @param bancomatRequest the Bancomat request
     * @return the created Bancomat details
     * @throws NotFoundException                if the account is not found
     * @throws BancomatAlreadyExistingException if the Bancomat already exists
     */
    @WebResult(name = "CreateBancomatResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:CreateBancomat")
    public BancomatResponse createBancomat(@XmlElement(required = true) @WebParam(name = "bancomatRequest",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") BancomatRequest bancomatRequest) throws NotFoundException, BancomatAlreadyExistingException;

    /**
     * Executes a transaction.
     *
     * @param transactionRequest the transaction request
     * @return the transaction response
     * @throws NotFoundException if the Bancomat is not found
     * @throws ExpiredBancomatException if the Bancomat is expired
     */
    @WebResult(name = "ExecuteTransactionResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:ExecuteTransaction")
    public BancomatTransactionResponse executeTransaction(@XmlElement(required = true) @WebParam(name = "transactionRequest",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") TransactionRequest transactionRequest) throws NotFoundException, ExpiredBancomatException;

    /**
     * Retrieves the transactions of a Bancomat by account ID.
     *
     * @param accountId the account ID
     * @return the list of transactions
     */
    @WebResult(name = "GetBancomatTransactionsResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatTransactions")
    @ResponseWrapper(localName = "getBancomatTransactionsResponse",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatTransactionsResponse")
    public List<BancomatTransactionResponse> getBancomatTransactions(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId);

    /**
     * Asynchronously retrieves the transactions of a Bancomat by account ID.
     *
     * @param accountId    the account ID
     * @param asyncHandler the async handler
     * @return a Future representing the async operation
     * @throws BancomatException if a general Bancomat error occurs
     */
    @WebResult(name = "GetBancomatTransactionsResponseAsync",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatTransactionsAsync")
    @ResponseWrapper(localName = "getBancomatTransactionsResponse",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatTransactionsResponse")
    public Future<?> getBancomatTransactionsAsync(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId, AsyncHandler<GetBancomatTransactionsResponse> asyncHandler) throws BancomatException;
}
