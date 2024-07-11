package it.univaq.sose.bankingoperationsserviceprosumer.domain;

import it.univaq.sose.bankaccountservice.webservice.TransactionResponse;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

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
