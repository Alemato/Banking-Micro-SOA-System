package it.univaq.sose.bancomatservice.webservice;

import it.univaq.sose.bancomatservice.domain.dto.BancomatRequest;
import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import it.univaq.sose.bancomatservice.domain.dto.TransactionRequest;
import it.univaq.sose.bancomatservice.domain.dto.TransactionResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@WebService(name = "BancomatService")
public interface BancomatService {

    @WebResult(name = "GetBancomatDetailsResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatDetails")
    public BancomatResponse getBancomatDetails(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId) throws NotFoundException;

    @WebResult(name = "GetBancomatDetailsByNumberResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatDetailsByNumber")
    public BancomatResponse getBancomatDetailsByNumber(@XmlElement(required = true) @WebParam(name = "number",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") String number) throws NotFoundException;

    @WebResult(name = "CreateBancomatResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:CreateBancomat")
    public BancomatResponse createBancomat(@XmlElement(required = true) @WebParam(name = "bancomatRequest",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") BancomatRequest bancomatRequest) throws NotFoundException, BancomatAlradyExistingException;

    @WebResult(name = "ExecuteTransactionResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:ExecuteTransaction")
    TransactionResponse executeTransaction(@XmlElement(required = true) @WebParam(name = "transactionRequest",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") TransactionRequest transactionRequest) throws NotFoundException, ExpiredBancomatException;

    @WebResult(name = "GetBancomatTransactionsResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatTransactions")
    List<TransactionResponse> getBancomatTransactions(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId);

}
