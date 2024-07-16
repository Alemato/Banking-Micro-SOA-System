package it.univaq.sose.bankingserviceclient.commands;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import it.univaq.sose.accountservice.model.AccountResponse;
import it.univaq.sose.accountservice.model.TokenResponse;
import it.univaq.sose.accountservice.model.UserCredentials;
import it.univaq.sose.bankingoperationsserviceprosumer.model.CreateBancomatResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.model.OpenAccountRequest;
import it.univaq.sose.bankingoperationsserviceprosumer.model.OpenAccountResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.model.ReportBankAccountResponse;
import it.univaq.sose.bankingserviceclient.client.AccountServiceClient;
import it.univaq.sose.bankingserviceclient.client.BankingOperationsServiceClient;
import it.univaq.sose.bankingserviceclient.client.FinancialReportServiceClient;
import it.univaq.sose.bankingserviceclient.client.LoanServiceClient;
import it.univaq.sose.bankingserviceclient.model.AccountDetails;
import it.univaq.sose.bankingserviceclient.security.AccountSession;
import it.univaq.sose.bankingserviceclient.security.JwtTokenProvider;
import it.univaq.sose.bankingserviceclient.util.GatewayUtil;
import it.univaq.sose.bankingserviceclient.util.InputReader;
import it.univaq.sose.bankingserviceclient.util.TableFormatter;
import it.univaq.sose.financialreportserviceprosumer.model.FinancialReportResponse;
import it.univaq.sose.loanserviceprosumer.model.LoanDto;
import it.univaq.sose.loanserviceprosumer.model.OpenLoanRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@ShellComponent
public class AccountCommands {

    private final AccountServiceClient accountServiceClient;
    private final BankingOperationsServiceClient bankingOperationsServiceClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountSession accountSession;
    private final FinancialReportServiceClient financialReportServiceClient;
    private final LoanServiceClient loanServiceClient;
    private final GatewayUtil gatewayUtil;


    public AccountCommands(AccountServiceClient accountServiceClient, BankingOperationsServiceClient bankingOperationsServiceClient, JwtTokenProvider jwtTokenProvider, AccountSession accountSession, FinancialReportServiceClient financialReportServiceClient, LoanServiceClient loanServiceClient, GatewayUtil gatewayUtil) {
        this.accountServiceClient = accountServiceClient;
        this.bankingOperationsServiceClient = bankingOperationsServiceClient;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountSession = accountSession;
        this.financialReportServiceClient = financialReportServiceClient;
        this.loanServiceClient = loanServiceClient;
        this.gatewayUtil = gatewayUtil;
    }

    @ShellMethod("Login to the banking system")
    public String login() {
        /*String username = InputReader.singleReadInput("Enter your username: ");
        String password = InputReader.singleReadInput("Enter your password: ");*/

        String username = "MarRos";
        String password = "123456";

        boolean loginSuccess = executeLogin(username, password);
        AccountDetails accountDetails = accountSession.getAccountDetails();
        log.error("accountDetails {}", accountDetails);

        /*
        executeFinancialReport(accountDetails);
        accountDetails = accountSession.getAccountDetails();
        */
        CreateBancomatResponse createBancomatResponse;
        try {
            executeRequestAtmCard(accountDetails);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //return TableFormatter.formatObjectDetails(accountDetails);

        return loginSuccess ? formatSuccessMessage("Login successful. Welcome " + accountDetails.getUsername() + "!") : formatErrorMessage("Login failed, please retry!");
    }

    @ShellMethod(key = "get-personal-account", value = "Show personal customer Account")
    public String getPersonalCustomerAccount() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        return TableFormatter.formatObjectDetails(accountDetails, "Account");
    }

    @ShellMethod(key = "get-account", value = "Get account details")
    public String getAccountByAccountId() {
        Long id;
        try {
            id = Long.parseLong(InputReader.singleReadInput("Enter the Account id: "));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input! Please try again");
        }
        WebClient client = accountServiceClient.getWebClientWithAuth();
        String url = client.getBaseURI() + "/api/account/" + id;
        WebClient targetClient = client.replacePath(url);
        AccountResponse response = targetClient.get(AccountResponse.class);

        return TableFormatter.formatObjectDetails(response, "Account");
    }

    @ShellMethod(key = "open-bank-account", value = "Get account details")
    public String createBankAccount() {
        executeOpenAccountResponse();
        AccountDetails accountDetails = accountSession.getAccountDetails();
        return TableFormatter.formatObjectDetails(accountDetails, "Account");
    }

    @ShellMethod("Show Financial Report")
    public String financialReport() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        try {
            executeFinancialReport(accountDetails);
        } catch (BankingOperationServiceException e) {
            throw new RuntimeException(e);
        }
        accountDetails = accountSession.getAccountDetails();
        return TableFormatter.formatObjectDetails(accountDetails, "Account");
    }

    @ShellMethod("Show Financial Report")
    public String bankAccountReport() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        ReportBankAccountResponse reportBankAccountResponse;
        try {
            reportBankAccountResponse = executeBankAccountReport(accountDetails);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        accountDetails = accountSession.getAccountDetails();
        return TableFormatter.formatObjectDetails(accountDetails, "Account") + TableFormatter.formatObjectDetails(reportBankAccountResponse.getTransactions(), "Transactions");
    }

    @ShellMethod(key = "open-loan", value = "Open Loan")
    public String openLoan() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        executeOpenLoan();
        accountDetails = accountSession.getAccountDetails();
        return TableFormatter.formatObjectDetails(accountDetails.getLoans(), "Loans");
    }

    @ShellMethod(key = "close-loan", value = "Close Loan")
    public String closeLoan() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        executeCloseLoan();
        return TableFormatter.formatObjectDetails(accountDetails.getLoans(), "Loans");
    }

    private String formatSuccessMessage(String message) {
        return "\n************ SUCCESS ***********\n" + message + "\n*******************************\n";
    }

    private String formatErrorMessage(String message) {
        return "\n************* ERROR ***********\n" + message + "\n********************************\n";
    }

    private Boolean executeLogin(String username, String password) {
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(username);
        credentials.setPassword(password);

        WebClient client = WebClient.create(gatewayUtil.getAccountServiceUrl(), List.of(new JacksonJsonProvider()));
        try (Response response = client.path("/login")
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON))) {

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                TokenResponse tokenResponse = response.readEntity(TokenResponse.class);
                jwtTokenProvider.setToken(tokenResponse.getToken());
                accountSession.updateAccountDetailsFromJwt(jwtTokenProvider.decodeToken());
                log.info("Login successful. Token: {}", tokenResponse.getToken());
                return true;
            } else {
                log.error("Login failed: {}", response.getStatusInfo().getReasonPhrase());
                return false;
            }
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            return false;
        }
    }

    private void executeFinancialReport(AccountDetails accountDetails) throws BankingOperationServiceException {
        try {
            WebClient client = financialReportServiceClient.getWebClientWithAuth();
            String url = client.getBaseURI() + "/api/bank/financial-report/" + accountDetails.getId();
            WebClient targetClient = client.replacePath(url);
            FinancialReportResponse response = targetClient.get(FinancialReportResponse.class);

            accountSession.updateAccountDetailsFromFinancialReport(response);
        } catch (Exception e) {
            log.error("Error fetching financial report", e);
            throw new BankingOperationServiceException("Error fetching financial report: " + e.getMessage());
        }
    }

    private ReportBankAccountResponse executeBankAccountReport(AccountDetails accountDetails) throws InterruptedException {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getBankingOperationServiceUrl() + "/report-bank-account-by-account/" + accountDetails.getId();
            String token = jwtTokenProvider.getToken();

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token);

            Future<Response> futureResponse = requestBuilder.async().get();
            log.error("futureResponse: {}", futureResponse);

            Response response = futureResponse.get();
            log.error("response: {}", response);

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                ReportBankAccountResponse reportBankAccountResponse = response.readEntity(ReportBankAccountResponse.class);
                accountSession.updateAccountDetailsFromReportBankAccount(reportBankAccountResponse);
                return reportBankAccountResponse;
            } else {
                log.error("Error response: {}", response.getStatusInfo().getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching financial report", e);
            throw new InterruptedException("Error fetching bank account report: " + e.getMessage());
        }
    }

    private CreateBancomatResponse executeRequestAtmCard(AccountDetails accountDetails) throws InterruptedException {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getBankingOperationServiceUrl() + "/atm-card/" + accountDetails.getId();
            String token = jwtTokenProvider.getToken();

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Content-Type", MediaType.APPLICATION_JSON);  // Ensure the Content-Type header is set

            Future<Response> futureResponse = requestBuilder.async().post(Entity.entity("", MediaType.APPLICATION_JSON));
            log.error("futureResponse: {}", futureResponse);

            Response response = futureResponse.get();
            log.error("response: {}", response);

            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                CreateBancomatResponse createBancomatResponse = response.readEntity(CreateBancomatResponse.class);
                accountSession.updateAccountDetailsFromCreateBancomat(createBancomatResponse);
                return createBancomatResponse;
            } else {
                log.error("Error response: {}", response.getStatusInfo().getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching bancomat", e);
            throw new InterruptedException("Error fetching bancomat: " + e.getMessage());
        }
    }


    private void executeOpenAccountResponse() {
        OpenAccountResponse openAccountResponse;
        try {
            OpenAccountRequest newAccount = InputReader.multipleReadInputs(OpenAccountRequest.class);

            WebClient client = bankingOperationsServiceClient.getWebClientBankingOperationsService();

            try (Response response = client.path("/api/bank/open-account").post(newAccount)) {
                if (!response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
                    throw new BankingOperationServiceException("Error for Banking Operation Service: " + response.getStatus());
                openAccountResponse = response.readEntity(OpenAccountResponse.class);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid input! Please try again");
        }
        accountSession.updateAccountDetailsFromOpenBankAccount(openAccountResponse);
    }

    private void executeOpenLoan() {
        try {
            OpenLoanRequest newAccount = InputReader.multipleReadInputs(OpenLoanRequest.class);

            WebClient client = loanServiceClient.getWebClientWithAuth();

            try (Response response = client.path("/api/loan/").post(newAccount)) {
                if (!response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
                    throw new LoanServiceException("Error for Loan Service (Open Loan): " + response.getStatus());
                LoanDto loan = response.readEntity(LoanDto.class);
                accountSession.updateAccountDetailsFromLoan(loan);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid input! Please try again");
        }
    }

    private void executeCloseLoan() {
        try {
            String idLoan = InputReader.singleReadInput("idLoan");
            WebClient client = loanServiceClient.getWebClientWithAuth();

            try (Response response = client.path("/api/loan/close-loan/" + idLoan).put(null)) {
                if (!response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
                    throw new LoanServiceException("Error for Loan Service (Close Loan) : " + response.getStatus());
                LoanDto loan = response.readEntity(LoanDto.class);
                accountSession.updateAccountDetailsFromLoan(loan);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid input! Please try again");
        }
    }
}
