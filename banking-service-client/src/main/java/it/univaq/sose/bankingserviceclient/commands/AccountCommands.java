package it.univaq.sose.bankingserviceclient.commands;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import it.univaq.sose.accountservice.model.TokenResponse;
import it.univaq.sose.accountservice.model.UserCredentials;
import it.univaq.sose.bankingoperationsserviceprosumer.model.CreateBancomatResponse;
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
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.Future;

;

@Slf4j
@ShellComponent
public class AccountCommands {

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountSession accountSession;
    private final GatewayUtil gatewayUtil;

    public AccountCommands(JwtTokenProvider jwtTokenProvider, AccountSession accountSession, GatewayUtil gatewayUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountSession = accountSession;
        this.gatewayUtil = gatewayUtil;
    }

    @ShellMethod("Login to the banking system")
    public String login() {
        String username = InputReader.singleReadInput("Enter your username: ");
        String password = InputReader.singleReadInput("Enter your password: ");

        boolean loginSuccess = executeLogin(username, password);
        AccountDetails accountDetails = accountSession.getAccountDetails();

        try {
            executeFinancialReport(accountDetails);
        } catch (InterruptedException e) {
            System.out.println("error: " + e.getMessage());
        }
        accountDetails = accountSession.getAccountDetails();
        System.out.println(loginSuccess ? formatSuccessMessage("Login successful. Welcome " + accountDetails.getUsername() + "!") : formatErrorMessage("Login failed, please retry!"));
        return TableFormatter.formatObjectDetails(accountDetails, "Account");
    }

    @ShellMethod(key = "open-bank-account", value = "Get account details")
    public String createBankAccount() throws InterruptedException {
        OpenAccountDTO newAccount = executeOpenAccountResponse();
        executeLogin(newAccount.getUsername(), newAccount.getPassword());
        AccountDetails accountDetails = accountSession.getAccountDetails();
        return TableFormatter.formatObjectDetails(accountDetails, "Account Creato");
    }

    @ShellMethod("Show Financial Report")
    @ShellMethodAvailability("isAuthenticated")
    public String financialReport() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        FinancialReportResponse financialReport;
        try {
            financialReport = executeFinancialReport(accountDetails);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return TableFormatter.formatObjectDetails(financialReport, "Financial Report");
    }

    @ShellMethod("Show Bank Account Report")
    @ShellMethodAvailability("isAuthenticated")
    public String bankAccountReport() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        ReportBankAccountResponse reportBankAccountResponse;
        try {
            reportBankAccountResponse = executeBankAccountReport(accountDetails);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        accountDetails = accountSession.getAccountDetails();
        System.out.println(TableFormatter.formatObjectDetails(accountDetails, "Account"));
        return TableFormatter.formatObjectDetails(reportBankAccountResponse.getTransactions(), "Transactions");
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

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
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

    private FinancialReportResponse executeFinancialReport(AccountDetails accountDetails) throws InterruptedException {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getFinancialReportServiceUrl() + "/" + accountDetails.getId();
            String token = jwtTokenProvider.getToken();

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token);

            Future<Response> futureResponse = requestBuilder.async().get();

            //while (!futureResponse.isDone()) {

            //}

            Response response = gatewayUtil.getAsyncResponseNotBlockingPolling(futureResponse);
//            Response response = futureResponse.get();
            log.error("response executeFinancialReport: {}", response);

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                FinancialReportResponse financialReportResponse = response.readEntity(FinancialReportResponse.class);
                accountSession.updateAccountDetailsFromFinancialReport(financialReportResponse);
                return financialReportResponse;
            } else {
                log.error("Error response: {}", response.getStatusInfo().getReasonPhrase());
                throw new RuntimeException("Error fetching financial report: ");
            }
        } catch (Exception e) {
            log.error("Error fetching financial report", e);
            throw new InterruptedException("Error fetching bank account report: " + e.getMessage());
        }
    }

    private ReportBankAccountResponse executeBankAccountReport(AccountDetails accountDetails) throws InterruptedException {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getBankingOperationServiceUrl() + "/report-bank-account-by-account/" + accountDetails.getId();
            String token = jwtTokenProvider.getToken();

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token);

            Future<Response> futureResponse = requestBuilder.async().get();

            Response response = futureResponse.get();
            log.error("response executeBankAccountReport: {}", response);

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                ReportBankAccountResponse reportBankAccountResponse = response.readEntity(ReportBankAccountResponse.class);
                accountSession.updateAccountDetailsFromReportBankAccount(reportBankAccountResponse);
                return reportBankAccountResponse;
            } else {
                log.error("Error response: {}", response.getStatusInfo().getReasonPhrase());
                throw new RuntimeException("Error fetching bank account report: ");
            }
        } catch (Exception e) {
            log.error("Error fetching financial report", e);
            throw new InterruptedException("Error fetching bank account report: " + e.getMessage());
        }
    }

    private void executeRequestAtmCard(AccountDetails accountDetails) {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getBankingOperationServiceUrl() + "/atm-card/" + accountDetails.getId();
            String token = jwtTokenProvider.getToken();

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Content-Type", MediaType.APPLICATION_JSON);

            Future<Response> futureResponse = requestBuilder.async().post(Entity.entity("", MediaType.APPLICATION_JSON));

            Response response = futureResponse.get();
            log.error("response executeRequestAtmCard : {}", response);

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                CreateBancomatResponse createBancomatResponse = response.readEntity(CreateBancomatResponse.class);
                log.error("createBancomatResponse: {}", createBancomatResponse);
                accountSession.updateAccountDetailsFromCreateBancomat(createBancomatResponse);
            } else {
                executeGetAtmCard(accountDetails);
                log.error("Error response: {}", response.getStatusInfo().getReasonPhrase());
            }
        } catch (Exception e) {
            log.error("Error fetching bancomat", e);
        }
    }

    private void executeGetAtmCard(AccountDetails accountDetails) throws InterruptedException {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getBankingOperationServiceUrl() + "/atm-card/" + accountDetails.getId();
            String token = jwtTokenProvider.getToken();

            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token);

            Future<Response> futureResponse = requestBuilder.async().get();

            Response response = futureResponse.get();
            log.error("response executeGetAtmCard: {}", response.getStatus());

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                CreateBancomatResponse createBancomatResponse = response.readEntity(CreateBancomatResponse.class);
                accountSession.updateAccountDetailsFromCreateBancomat(createBancomatResponse);
            } else {
                log.error("Error response: {}", response.getStatusInfo().getReasonPhrase());
            }
        } catch (Exception e) {
            log.error("Error fetching financial report", e);
            throw new InterruptedException("Error fetching bank account report: " + e.getMessage());
        }
    }

    private OpenAccountDTO executeOpenAccountResponse() throws InterruptedException {
        OpenAccountDTO newAccount = InputReader.multipleReadInputs(OpenAccountDTO.class);
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
            log.error("futureResponse: {}", futureResponse);

            Response response = gatewayUtil.getAsyncResponseNotBlockingPolling(futureResponse);
//            Response response = futureResponse.get();
            log.error("response: {}", response);

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                OpenAccountResponse openAccountResponse = response.readEntity(OpenAccountResponse.class);
                log.error("openAccountResponse: {}", openAccountResponse);
                accountSession.updateAccountDetailsFromOpenBankAccount(openAccountResponse);
            } else {
                log.error("Error response: {}", response.getStatusInfo().getReasonPhrase());
            }
            return newAccount;
        } catch (Exception e) {
            log.error("Error fetching account", e);
            throw new InterruptedException("Error fetching account: " + e.getMessage());
        }

    }


    private Availability isAuthenticated() {
        return accountSession.isLoggedIn()
                ? Availability.available()
                : Availability.unavailable("You are not logged in");
    }
}
