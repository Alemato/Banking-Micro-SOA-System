package it.univaq.sose.bancomatservice.webservice;

import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import it.univaq.sose.bancomatservice.domain.dto.TransactionRequest;
import it.univaq.sose.bancomatservice.domain.dto.TransactionResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.ws.RequestWrapper;

import java.util.List;

@WebService(name = "BancomatService")
public interface BancomatService {

    @WebResult(name = "GetBancomatDetailsResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @RequestWrapper(localName = "getBancomatRequest",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatRequest")
    @WebMethod(action = "urn:GetBancomatDetails")
    public BancomatResponse getBancomatDetails(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId) throws NotFoundException;

    @WebResult(name = "CreateBancomatResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @RequestWrapper(localName = "getBancomatRequest",
            className = "it.univaq.sose.bancomatservice.domain.dto.GetBancomatRequest")
    @WebMethod(action = "urn:CreateBancomat")
    public BancomatResponse createBancomat(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId) throws NotFoundException, BancomatAlradyExistingException;

    @WebResult(name = "ExecuteTransactionResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:ExecuteTransaction")
    TransactionResponse executeTransaction(@XmlElement(required = true) @WebParam(name = "balanceUpdateRequest",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") TransactionRequest transactionRequest) throws NotFoundException, ExpiredBancomatException;

    @WebResult(name = "GetBancomatTransactionsResponse",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/")
    @WebMethod(action = "urn:GetBancomatTransactions")
    List<TransactionResponse> getBancomatTransactions(@XmlElement(required = true) @WebParam(name = "accountId",
            targetNamespace = "http://webservice.bancomatservice.sose.univaq.it/") Long accountId);

}
