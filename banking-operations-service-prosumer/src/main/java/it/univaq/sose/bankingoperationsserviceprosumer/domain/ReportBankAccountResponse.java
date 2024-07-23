package it.univaq.sose.bankingoperationsserviceprosumer.domain;

import it.univaq.sose.bankaccountservice.webservice.TransactionResponse;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) for the response of a bank account report request.
 * This class contains all the necessary information provided in the response, including account details, bank account details, and transactions.
 */
@Data
@XmlRootElement(name = "ReportBankAccountResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportBankAccountResponse {
    @XmlElement(required = true)
    private AccountDetails account;
    @XmlElement(required = true)
    private BankAccountDetails bankAccount;
    @XmlElement(required = true)
    private List<TransactionResponse> transactions;
}
