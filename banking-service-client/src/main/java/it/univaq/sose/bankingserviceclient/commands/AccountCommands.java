package it.univaq.sose.bankingserviceclient.commands;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import it.univaq.sose.accountservice.model.TokenResponse;
import it.univaq.sose.accountservice.model.UserCredentials;
import it.univaq.sose.bankingoperationsserviceprosumer.model.OpenAccountRequest;
import it.univaq.sose.bankingoperationsserviceprosumer.model.OpenAccountResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.model.ReportBankAccountResponse;
import it.univaq.sose.bankingserviceclient.model.AccountDetails;
import it.univaq.sose.bankingserviceclient.model.OpenAccountDTO;
import it.univaq.sose.bankingserviceclient.security.AccountSession;
import it.univaq.sose.bankingserviceclient.security.JwtTokenProvider;
import it.univaq.sose.bankingserviceclient.util.GatewayUtil;
import it.univaq.sose.bankingserviceclient.util.InputReader;
import it.univaq.sose.bankingserviceclient.util.TableFormatter;
import it.univaq.sose.financialreportserviceprosumer.model.FinancialReportResponse;
import it.univaq.sose.transactionserviceprosumer.model.BalanceUpdateRequest;
import it.univaq.sose.transactionserviceprosumer.model.ExecuteTransactionResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@ShellComponent
public class AccountCommands extends AbstractShellComponent {

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountSession accountSession;
    private final GatewayUtil gatewayUtil;

    public AccountCommands(JwtTokenProvider jwtTokenProvider, AccountSession accountSession, GatewayUtil gatewayUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountSession = accountSession;
        this.gatewayUtil = gatewayUtil;
    }

    @ShellMethod(key = "login", value = "Login to the banking system")
    public String login() {
        String username = InputReader.singleReadInput(getTerminal(), "Enter your username: ");
        String password = InputReader.singleReadInput(getTerminal(), "Enter your password: ");

        try {
            executeLogin(username, password);
        } catch (BankingClientException e) {
            return gatewayUtil.formatErrorMessage(e.getMessage());
        }
        AccountDetails accountDetails = accountSession.getAccountDetails();
        try {
            executeFinancialReport(accountDetails);
        } catch (BankingClientException e) {
            return gatewayUtil.formatErrorMessage(e.getMessage());
        }

        accountDetails = accountSession.getAccountDetails();
        int width = getTerminal().getWidth();
        String msg;
        System.out.println(gatewayUtil.formatSuccessMessage("Login successful. Welcome " + accountDetails.getUsername() + "!"));
        return TableFormatter.formatObjectDetails(getTerminal(), accountDetails, "Account");
    }

    @ShellMethod(key = "open-bank-account", value = "Open Bank Account")
    public String createBankAccount() throws InterruptedException {
        OpenAccountDTO newAccount = null;
        try {
            newAccount = executeOpenAccountResponse();
        } catch (BankingClientException e) {
            throw new RuntimeException(e);
        }

        try {
            executeLogin(newAccount.getUsername(), newAccount.getPassword());
        } catch (BankingClientException e) {
            return gatewayUtil.formatErrorMessage(e.getMessage());
        }
        AccountDetails accountDetails = accountSession.getAccountDetails();
        return TableFormatter.formatObjectDetails(getTerminal(), accountDetails, "Account Creato");
    }

    @ShellMethod(key = "financial-report", value = "Show Financial Report")
    @ShellMethodAvailability("isAuthenticated")
    public String financialReport() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        FinancialReportResponse financialReport;
        try {
            financialReport = executeFinancialReport(accountDetails);
        } catch (BankingClientException e) {
            return gatewayUtil.formatErrorMessage(e.getMessage());
        }
        return TableFormatter.formatObjectDetails(getTerminal(), financialReport, "Financial Report");
    }

    @ShellMethod(key = "bank-account-report", value = "Show Bank Account Report")
    @ShellMethodAvailability("isAuthenticated")
    public String bankAccountReport() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        ReportBankAccountResponse reportBankAccountResponse;
        try {
            reportBankAccountResponse = executeBankAccountReport(accountDetails);
        } catch (BankingClientException e) {
            return gatewayUtil.formatErrorMessage(e.getMessage());
        }

        System.out.println(TableFormatter.formatObjectDetails(getTerminal(), accountSession.getAccountDetails(), "Account"));
        return TableFormatter.formatObjectDetails(getTerminal(), reportBankAccountResponse.getTransactions(), "Transactions");
    }

    @ShellMethodAvailability("isAuthenticated")
    @ShellMethod(key = "withdrawal", value = "Show Bank Account Report")
    public String withdrawal() {
        AccountDetails accountDetails = accountSession.getAccountDetails();

        ExecuteTransactionResponse executeTransactionResponse;
        try {
            executeTransactionResponse = executeWithdrawal(accountDetails);
        } catch (BankingClientException e) {
            return gatewayUtil.formatErrorMessage(e.getMessage());
        }
        if (executeTransactionResponse != null) {
            return TableFormatter.formatObjectDetails(getTerminal(), executeTransactionResponse, "Withdrawal");
        }
        return "";
    }

    private Boolean executeLogin(String username, String password) throws BankingClientException {
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(username);
        credentials.setPassword(password);

        WebClient client = WebClient.create(gatewayUtil.getAccountServiceUrl(), List.of(new JacksonJsonProvider()));
        try (Response response = client.path("/login")
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON))) {

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                TokenResponse tokenResponse = response.readEntity(TokenResponse.class);
                jwtTokenProvider.setToken(tokenResponse.getToken());

                accountSession.updateAccountDetailsFromJwt(jwtTokenProvider.decodeToken());
                log.info("Login successful. Token: {}", tokenResponse.getToken());
                return true;
            } else {
                throw new BankingClientException(gatewayUtil.extractErrorMessage(response.readEntity(String.class)));
            }
        } catch (Exception e) {
            throw new BankingClientException(e.getMessage());
        }
    }

    private FinancialReportResponse executeFinancialReport(AccountDetails accountDetails) throws BankingClientException {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getFinancialReportServiceUrl() + "/" + accountDetails.getId();
            String token = jwtTokenProvider.getToken();

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token);

            Future<Response> futureResponse = requestBuilder.async().get();
            Response response = gatewayUtil.getAsyncResponseNotBlockingPolling(futureResponse);

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                FinancialReportResponse financialReportResponse = response.readEntity(FinancialReportResponse.class);
                accountSession.updateAccountDetailsFromFinancialReport(financialReportResponse);
                return financialReportResponse;
            } else {
                throw new BankingClientException(gatewayUtil.extractErrorMessage(response.readEntity(String.class)));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new BankingClientException(e.getMessage());
        }
    }

    private ReportBankAccountResponse executeBankAccountReport(AccountDetails accountDetails) throws BankingClientException {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getBankingOperationServiceUrl() + "/report-bank-account-by-account/" + accountDetails.getId();
            String token = jwtTokenProvider.getToken();

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token);

            Future<Response> futureResponse = requestBuilder.async().get();

            Response response = gatewayUtil.getAsyncResponseNotBlockingPolling(futureResponse);

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                ReportBankAccountResponse reportBankAccountResponse = response.readEntity(ReportBankAccountResponse.class);
                accountSession.updateAccountDetailsFromReportBankAccount(reportBankAccountResponse);
                return reportBankAccountResponse;
            } else {
                throw new BankingClientException(gatewayUtil.extractErrorMessage(response.readEntity(String.class)));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new BankingClientException(e.getMessage());
        }
    }

    private OpenAccountDTO executeOpenAccountResponse() throws BankingClientException {
        OpenAccountDTO newAccount = InputReader.multipleReadInputs(getTerminal(), OpenAccountDTO.class);
        OpenAccountRequest openAccountRequest = new OpenAccountRequest();
        openAccountRequest.setName(openAccountRequest.getName());
        openAccountRequest.setSurname(openAccountRequest.getSurname());
        openAccountRequest.setUsername(openAccountRequest.getUsername());
        openAccountRequest.setPassword(openAccountRequest.getPassword());
        openAccountRequest.setEmail(openAccountRequest.getEmail());
        openAccountRequest.setPhone(openAccountRequest.getPhone());
        openAccountRequest.setBalance(openAccountRequest.getBalance());
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getBankingOperationServiceUrl() + "/open-account";

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Content-Type", MediaType.APPLICATION_JSON);

            Future<Response> futureResponse = requestBuilder.async().post(Entity.entity(newAccount, MediaType.APPLICATION_JSON));
            Response response = gatewayUtil.getAsyncResponseNotBlockingPolling(futureResponse);

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                OpenAccountResponse openAccountResponse = response.readEntity(OpenAccountResponse.class);
                accountSession.updateAccountDetailsFromOpenBankAccount(openAccountResponse);
            } else {
                throw new BankingClientException(gatewayUtil.extractErrorMessage(response.readEntity(String.class)));
            }
            return newAccount;
        } catch (InterruptedException | ExecutionException e) {
            throw new BankingClientException(e.getMessage());
        }
    }

    private ExecuteTransactionResponse executeWithdrawal(AccountDetails accountDetails) throws BankingClientException {

        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(InputReader.singleReadInput(getTerminal(), "Enter amount: ")));
        String description = InputReader.singleReadInput(getTerminal(), "Enter description: ");

        BalanceUpdateRequest balanceUpdateRequest = new BalanceUpdateRequest()
                .bankAccountId(accountDetails.getBankAccount().getId())
                .amount(amount)
                .description(description);

        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getTransactionServiceUrl() + "/withdraw-money";

            String token = jwtTokenProvider.getToken();

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Content-Type", MediaType.APPLICATION_JSON);

            Future<Response> futureResponse = requestBuilder.async().post(Entity.entity(balanceUpdateRequest, MediaType.APPLICATION_JSON));

            Response response = gatewayUtil.getAsyncResponseNotBlockingPolling(futureResponse);

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                return response.readEntity(ExecuteTransactionResponse.class);
            } else {
                throw new BankingClientException(gatewayUtil.extractErrorMessage(response.readEntity(String.class)));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new BankingClientException(e.getMessage());
        }
    }


    private Availability isAuthenticated() {
        return accountSession.isLoggedIn()
                ? Availability.available()
                : Availability.unavailable("You are not logged in");
    }
}
