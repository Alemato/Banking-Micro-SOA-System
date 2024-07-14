package it.univaq.sose.bancomatservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Classe Java per getBancomatTransactionsResponse complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="getBancomatTransactionsResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetBancomatTransactionsResponse" type="{http://webservice.bancomatservice.sose.univaq.it/}TransactionResponse" maxOccurs="unbounded" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBancomatTransactionsResponse", propOrder = {
        "getBancomatTransactionsResponse"
})
public class GetBancomatTransactionsResponse {

    @XmlElement(name = "GetBancomatTransactionsResponse", namespace = "http://webservice.bancomatservice.sose.univaq.it/")
    protected List<TransactionResponse> getBancomatTransactionsResponse;

    /**
     * Gets the value of the getBancomatTransactionsResponse property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the getBancomatTransactionsResponse property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGetBancomatTransactionsResponse().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransactionResponse }
     */
    public List<TransactionResponse> getGetBancomatTransactionsResponse() {
        if (getBancomatTransactionsResponse == null) {
            getBancomatTransactionsResponse = new ArrayList<TransactionResponse>();
        }
        return this.getBancomatTransactionsResponse;
    }

}
