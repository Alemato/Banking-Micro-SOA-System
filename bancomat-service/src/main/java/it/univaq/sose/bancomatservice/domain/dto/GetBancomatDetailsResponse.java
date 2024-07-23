package it.univaq.sose.bancomatservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Data Transfer Object (DTO) for the response of the getBancomatDetails operation.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBancomatDetailsResponse", propOrder = {
        "getBancomatDetailsResponse"
})
public class GetBancomatDetailsResponse {

    @XmlElement(name = "GetBancomatDetailsResponse", namespace = "http://webservice.bancomatservice.sose.univaq.it/")
    protected BancomatResponse getBancomatDetailsResponse;

    /**
     * The BancomatResponse containing the details of the requested Bancomat
     *
     * @return possible object is
     * {@link BancomatResponse }
     */
    public BancomatResponse getGetBancomatDetailsResponse() {
        return getBancomatDetailsResponse;
    }

    /**
     * Sets the BancomatResponse containing the details of the requested Bancomat.
     *
     * @param value allowed object is
     *              {@link BancomatResponse }
     */
    public void setGetBancomatDetailsResponse(BancomatResponse value) {
        this.getBancomatDetailsResponse = value;
    }

}
