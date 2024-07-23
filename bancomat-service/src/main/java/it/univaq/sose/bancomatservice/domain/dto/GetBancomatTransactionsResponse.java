package it.univaq.sose.bancomatservice.domain.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) for the response of the getBancomatTransactions operation.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBancomatTransactionsResponse", propOrder = {
        "getBancomatTransactionsResponse"
})
public class GetBancomatTransactionsResponse {

    @XmlElement(name = "GetBancomatTransactionsResponse", namespace = "http://webservice.bancomatservice.sose.univaq.it/")
    protected List<BancomatTransactionResponse> getBancomatTransactionsResponse;

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
     * {@link BancomatTransactionResponse }
     */
    public List<BancomatTransactionResponse> getGetBancomatTransactionsResponse() {
        if (getBancomatTransactionsResponse == null) {
            getBancomatTransactionsResponse = new ArrayList<BancomatTransactionResponse>();
        }
        return this.getBancomatTransactionsResponse;
    }

}
