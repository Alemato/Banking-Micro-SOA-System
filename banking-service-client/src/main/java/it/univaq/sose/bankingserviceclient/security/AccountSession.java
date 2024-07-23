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
import java.util.Optional;

/**
 * This class manages the session for a logged-in account, including updating and resetting session details.
 */
@Component
public class AccountSession {
    @Getter
    private boolean isLoggedIn = false;

    private AccountDetails accountDetails;

    public void AccountSession(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
        this.isLoggedIn = true;
    }

    /**
     * Gets the account details for the current session.
     *
     * @return the account details
     */
    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    /**
     * Resets the session by clearing the account details and marking the user as logged out.
     */
    public void resetAccountSession() {
        accountDetails = new AccountDetails();
        isLoggedIn = false;
    }

    /**
     * Updates the account details from JWT claims and marks the user as logged in.
     *
     * @param claims the JWT claims containing account information
     */
    public void updateAccountDetailsFromJwt(JwtClaims claims) {
        if (accountDetails == null) {
            accountDetails = new AccountDetails();
        }
        this.isLoggedIn = true;
        accountDetails.setId(Long.parseLong(claims.getClaim("identifier").toString()));
        accountDetails.setUsername(claims.getSubject());
        accountDetails.setRole(claims.getClaim("role").toString());
    }

    /**
     * Updates the account details from a financial report response.
     *
     * @param financialReportResponse the response containing updated financial information
     */
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
                            ld.getBorrowerName(),
                            ld.getLoanStatus()));
        }
        accountDetails.setLoans(loans);
    }

    /**
     * Updates the account details from a bank account report response.
     *
     * @param reportBankAccountResponse the response containing updated bank account information
     */
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

    /**
     * Updates the account details from opening bank account response.
     *
     * @param openAccountResponse the response containing created bank account information
     */
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

    /**
     * Updates the account details from opened loan response.
     *
     * @param loan the response containing created loan information
     */
    public void updateAccountDetailsFromLoan(it.univaq.sose.loanserviceprosumer.model.LoanDto loan) {
        Optional<Loan> existingLoan = accountDetails.getLoans().stream()
                .filter(l -> l.getId().equals(loan.getId()))
                .findFirst();

        if (existingLoan.isPresent()) {
            Loan loanToUpdate = existingLoan.get();
            loanToUpdate.setAmount(loan.getAmount());
            loanToUpdate.setInterestRate(loan.getInterestRate());
            loanToUpdate.setTermInYears(loan.getTermInYears());
            loanToUpdate.setBorrowerName(loan.getBorrowerName());
            loanToUpdate.setLoanStatus(loan.getLoanStatus());
        } else {
            accountDetails.addLoan(new Loan(loan.getId(), loan.getAmount(), loan.getInterestRate(), loan.getTermInYears(), loan.getBorrowerName(), loan.getLoanStatus()));
        }
    }

    /**
     * Updates the account details from created bancomat response.
     *
     * @param createBancomatResponse the response containing created bancomat information
     */
    public void updateAccountDetailsFromCreateBancomat(CreateBancomatResponse createBancomatResponse) {
        accountDetails.setBancomat(new Bancomat(createBancomatResponse.getId(), createBancomatResponse.getNumber(), createBancomatResponse.getCvv(), createBancomatResponse.getDataScadenza()));
    }
}
