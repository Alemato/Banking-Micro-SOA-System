package it.univaq.sose.bankingserviceclient.security;

import it.univaq.sose.bankingoperationsserviceprosumer.model.CreateBancomatResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.model.OpenAccountResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.model.ReportBankAccountResponse;
import it.univaq.sose.bankingserviceclient.model.AccountDetails;
import it.univaq.sose.bankingserviceclient.model.Bancomat;
import it.univaq.sose.bankingserviceclient.model.BankAccount;
import it.univaq.sose.bankingserviceclient.model.Loan;
import it.univaq.sose.financialreportserviceprosumer.model.FinancialReportResponse;
import it.univaq.sose.financialreportserviceprosumer.model.LoanDto;
import lombok.Getter;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountSession {
    @Getter
    private boolean isLoggedIn = false;

    private AccountDetails accountDetails;

    public void AccountSession(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
        this.isLoggedIn = true;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void updateAccountDetailsFromJwt(JwtClaims claims) {
        if (accountDetails == null) {
            accountDetails = new AccountDetails();
        }
        this.isLoggedIn = true;
        accountDetails.setId(Long.parseLong(claims.getClaim("identifier").toString()));
        accountDetails.setUsername(claims.getSubject());
        accountDetails.setRole(claims.getClaim("role").toString());
    }

    public void updateAccountDetailsFromFinancialReport(FinancialReportResponse financialReportResponse) {
        accountDetails.setName(financialReportResponse.getAccount().getName());
        accountDetails.setSurname(financialReportResponse.getAccount().getSurname());
        accountDetails.setEmail(financialReportResponse.getAccount().getEmail());
        accountDetails.setPhone(financialReportResponse.getAccount().getPhone());

        accountDetails.setBankAccount(
                new BankAccount(
                        financialReportResponse.getBankAccount().getId(),
                        financialReportResponse.getBankAccount().getIban(),
                        financialReportResponse.getBankAccount().getBalance()));
        accountDetails.setBancomat(
                new Bancomat(
                        financialReportResponse.getBancomat().getId(),
                        financialReportResponse.getBancomat().getNumber(),
                        financialReportResponse.getBancomat().getCvv(),
                        financialReportResponse.getBancomat().getExpiryDate()));
        List<Loan> loans = new ArrayList<>();
        for (LoanDto ld : financialReportResponse.getLoans()) {
            loans.add(
                    new Loan(
                            ld.getId(),
                            ld.getAmount(),
                            ld.getInterestRate(),
                            ld.getTermInYears(),
                            ld.getBorrowerName()));
        }
        accountDetails.setLoans(loans);
    }

    public void updateAccountDetailsFromReportBankAccount(ReportBankAccountResponse reportBankAccountResponse) {
        accountDetails.setId(reportBankAccountResponse.getAccount().getId());
        accountDetails.setName(reportBankAccountResponse.getAccount().getName());
        accountDetails.setSurname(reportBankAccountResponse.getAccount().getSurname());
        accountDetails.setUsername(reportBankAccountResponse.getAccount().getUsername());
        accountDetails.setEmail(reportBankAccountResponse.getAccount().getEmail());
        accountDetails.setPhone(reportBankAccountResponse.getAccount().getPhone());

        accountDetails.setBankAccount(
                new BankAccount(
                        reportBankAccountResponse.getBankAccount().getId(),
                        reportBankAccountResponse.getBankAccount().getIban(),
                        reportBankAccountResponse.getBankAccount().getBalance()));
    }

    public void updateAccountDetailsFromOpenBankAccount(OpenAccountResponse openAccountResponse) {
        if (accountDetails == null) {
            accountDetails = new AccountDetails();
        }
        accountDetails.setId(openAccountResponse.getId());
        accountDetails.setName(openAccountResponse.getName());
        accountDetails.setSurname(openAccountResponse.getSurname());
        accountDetails.setUsername(openAccountResponse.getUsername());
        accountDetails.setEmail(openAccountResponse.getEmail());
        accountDetails.setPhone(openAccountResponse.getPhone());

        accountDetails.setBankAccount(
                new BankAccount(
                        openAccountResponse.getId(),
                        openAccountResponse.getIban(),
                        openAccountResponse.getBalance()));

        accountDetails.setBancomat(
                new Bancomat(
                        openAccountResponse.getBancomatId(),
                        openAccountResponse.getBancomatNumber(),
                        openAccountResponse.getBancomatCvv(),
                        openAccountResponse.getBancomatExpiryDate()));

    }

    public void updateAccountDetailsFromLoan(it.univaq.sose.loanserviceprosumer.model.LoanDto loan) {
        accountDetails.addLoan(new Loan(loan.getId(), loan.getAmount(), loan.getInterestRate(), loan.getTermInYears(), loan.getBorrowerName()));
    }

    public void updateAccountDetailsFromCreateBancomat(CreateBancomatResponse createBancomatResponse) {
        accountDetails.setBancomat(new Bancomat(createBancomatResponse.getId(), createBancomatResponse.getNumber(), createBancomatResponse.getCvv(), createBancomatResponse.getDataScadenza()));
    }
}
