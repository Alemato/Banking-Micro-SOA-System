package it.univaq.sose.bancomatservice.webservice;

import it.univaq.sose.bancomatservice.domain.dto.BancomatResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;

@WebService(name = "BancomatService")
public interface BancomatService {

    @WebResult(name = "BancomatResponse", targetNamespace = "")
    @WebMethod(action = "urn:GetBancomatDetails")
    public BancomatResponse getBancomatDetails(@XmlElement(required = true) @WebParam(name = "accountId", targetNamespace = "") Long accountId) throws NotFoundException;

    @WebResult(name = "BancomatResponse", targetNamespace = "")
    @WebMethod(action = "urn:CreateBancomat")
    public BancomatResponse createBancomat(@XmlElement(required = true) @WebParam(name = "accountId", targetNamespace = "") Long accountId) throws NotFoundException;


}
