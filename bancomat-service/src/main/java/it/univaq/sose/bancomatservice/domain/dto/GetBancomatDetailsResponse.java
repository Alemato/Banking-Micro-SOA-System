package it.univaq.sose.bancomatservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>Classe Java per getBancomatDetailsResponse complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="getBancomatDetailsResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetBancomatDetailsResponse" type="{http://webservice.bancomatservice.sose.univaq.it/}BancomatResponse" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBancomatDetailsResponse", propOrder = {
        "getBancomatDetailsResponse"
})
public class GetBancomatDetailsResponse {

    @XmlElement(name = "GetBancomatDetailsResponse", namespace = "http://webservice.bancomatservice.sose.univaq.it/")
    protected BancomatResponse getBancomatDetailsResponse;

    /**
     * Recupera il valore della proprietà getBancomatDetailsResponse.
     *
     * @return possible object is
     * {@link BancomatResponse }
     */
    public BancomatResponse getGetBancomatDetailsResponse() {
        return getBancomatDetailsResponse;
    }

    /**
     * Imposta il valore della proprietà getBancomatDetailsResponse.
     *
     * @param value allowed object is
     *              {@link BancomatResponse }
     */
    public void setGetBancomatDetailsResponse(BancomatResponse value) {
        this.getBancomatDetailsResponse = value;
    }

}
