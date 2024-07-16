package it.univaq.sose.financialreportserviceprosumer.domain;

import it.univaq.sose.bancomatservice.webservice.BancomatResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.model.AccountDetails;
import it.univaq.sose.bankingoperationsserviceprosumer.model.BankAccountDetails;
import it.univaq.sose.bankingoperationsserviceprosumer.model.TransactionResponse;
import it.univaq.sose.loanserviceprosumer.model.LoanDto;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "FinancialReportResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class FinancialReportResponse {
    @XmlElement(required = true)
    private AccountDetails account;

    @XmlElement(required = true)
    private BankAccountDetails bankAccount;

    @XmlElement(required = true)
    private List<TransactionResponse> bankAccountTransactions = new ArrayList<>();

    @XmlElement(required = true)
    private BancomatResponse bancomat;

    @XmlElement(required = true)
    private List<it.univaq.sose.bancomatservice.webservice.BancomatTransactionResponse> bancomatTransactions = new ArrayList<>();

    @XmlElement(required = true)
    private List<LoanDto> loans = new ArrayList<>();

    public FinancialReportResponse() {
    }

    public FinancialReportResponse(AccountDetails account, BankAccountDetails bankAccount, List<TransactionResponse> bankAccountTransactions, BancomatResponse bancomat, List<it.univaq.sose.bancomatservice.webservice.BancomatTransactionResponse> bancomatTransactions, List<LoanDto> loans) {
        this.account = account;
        this.bankAccount = bankAccount;
        this.bankAccountTransactions = bankAccountTransactions;
        this.bancomat = bancomat;
        this.bancomatTransactions = bancomatTransactions;
        this.loans = loans;
    }
}
