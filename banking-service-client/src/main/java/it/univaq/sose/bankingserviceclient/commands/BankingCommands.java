package it.univaq.sose.bankingserviceclient.commands;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import it.univaq.sose.accountservice.model.TokenResponse;
import it.univaq.sose.accountservice.model.UserCredentials;
import it.univaq.sose.bankingoperationsserviceprosumer.model.OpenAccountRequest;
import it.univaq.sose.bankingoperationsserviceprosumer.model.OpenAccountResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.model.ReportBankAccountResponse;
import it.univaq.sose.bankingserviceclient.model.AccountDetails;
import it.univaq.sose.bankingserviceclient.model.Loan;
import it.univaq.sose.bankingserviceclient.model.OpenAccountDTO;
import it.univaq.sose.bankingserviceclient.model.dto.OpenLoanRequestDTO;
import it.univaq.sose.bankingserviceclient.security.AccountSession;
import it.univaq.sose.bankingserviceclient.security.JwtTokenProvider;
import it.univaq.sose.bankingserviceclient.util.GatewayUtil;
import it.univaq.sose.bankingserviceclient.util.InputReader;
import it.univaq.sose.bankingserviceclient.util.TableFormatter;
import it.univaq.sose.bankingserviceclient.util.TerminalUtil;
import it.univaq.sose.financialreportserviceprosumer.model.FinancialReportResponse;
import it.univaq.sose.loanserviceprosumer.model.LoanDto;
import it.univaq.sose.loanserviceprosumer.model.OpenLoanRequest;
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
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Shell component providing banking commands for the user.
 */
@Slf4j
@ShellComponent
public class BankingCommands extends AbstractShellComponent {

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountSession accountSession;
    private final GatewayUtil gatewayUtil;

    public BankingCommands(JwtTokenProvider jwtTokenProvider, AccountSession accountSession, GatewayUtil gatewayUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountSession = accountSession;
        this.gatewayUtil = gatewayUtil;
    }

    /**
     * Login function.
     *
     * @return the availability status
     */
    @ShellMethodAvailability("isNotAuthenticated")
    @ShellMethod(key = "login", value = "Login to the banking system", group = "AUTHENTICATION OPERATIONS")
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
        TerminalUtil.printlnOnTerminal(getTerminal(), gatewayUtil.formatSuccessMessage("Login successful. Welcome " + accountDetails.getUsername() + "!"));
        return TableFormatter.formatObjectDetails(getTerminal(), accountDetails, "Account");
    }


    /**
     * Logs out the user from the banking system.
     *
     * @return the logout message
     */
    @ShellMethodAvailability("isAuthenticated")
    @ShellMethod(key = "logout", value = "Logout from the banking system", group = "AUTHENTICATION OPERATIONS")
    public String logout() {
        String key = InputReader.singleReadInputCustom(getTerminal(), "Are you sure you want to logout? (Y/n)");

        while (true) {
            if (Objects.equals(key, "n")) {
                return "\n****************************\nGood decision, stay with us!\n****************************\n";
            } else if (Objects.equals(key, "Y")) {
                accountSession.resetAccountSession();
                jwtTokenProvider.clearToken();
                return "\n**********************\nGoodbye, see you soon!\n**********************\n";
            } else {
                key = InputReader.singleReadInputCustom(getTerminal(), "Command not recognised, try again: (Y/n)");
            }
        }
    }

    /**
     * Opens a new bank account.
     *
     * @return the account creation details
     * @throws InterruptedException if the operation is interrupted
     */
    @ShellMethodAvailability("isNotAuthenticated")
    @ShellMethod(key = "open-bank-account", value = "Open Bank Account", group = "BANKING OPERATIONS")
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
        return TableFormatter.formatObjectDetails(getTerminal(), accountDetails, "Account Created");
    }

    /**
     * Shows complete the financial report of the current user.
     *
     * @return the financial report details
     */
    @ShellMethodAvailability("isAuthenticated")
    @ShellMethod(key = "financial-report", value = "Show Financial Report", group = "REPORT OPERATIONS")
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

    /**
     * Shows the bank account report of the current user.
     *
     * @return the bank account report details
     */
    @ShellMethodAvailability("isAuthenticated")
    @ShellMethod(key = "bank-account-report", value = "Show Bank Account Report", group = "REPORT OPERATIONS")
    public String bankAccountReport() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        ReportBankAccountResponse reportBankAccountResponse;
        try {
            reportBankAccountResponse = executeBankAccountReport(accountDetails);
        } catch (BankingClientException e) {
            return gatewayUtil.formatErrorMessage(e.getMessage());
        }

        TerminalUtil.printlnOnTerminal(getTerminal(), TableFormatter.formatObjectDetails(getTerminal(), accountSession.getAccountDetails(), "Account"));
        return TableFormatter.formatObjectDetails(getTerminal(), reportBankAccountResponse.getTransactions(), "Transactions");
    }

    /**
     * Executes a withdrawal operation.
     *
     * @return the withdrawal details
     */
    @ShellMethodAvailability("isAuthenticated")
    @ShellMethod(key = "withdrawal", value = "Withdrawal Operation", group = "BANKING OPERATIONS")
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

    /**
     * Opens a new loan.
     *
     * @return the loan details
     */
    @ShellMethodAvailability("isAuthenticated")
    @ShellMethod(key = "open-loan", value = "Open Loan", group = "LOAN OPERATION")
    public String openLoan() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        try {
            executeOpenLoan(accountDetails);
        } catch (BankingClientException e) {
            return gatewayUtil.formatErrorMessage(e.getMessage());
        }
        return TableFormatter.formatObjectDetails(getTerminal(), accountDetails.getLoans(), "Loans");
    }

    /**
     * Closes an existing loan.
     *
     * @return the loan closing details
     */
    @ShellMethodAvailability("isAuthenticatedAndLoanOpenedExisting")
    @ShellMethod(key = "close-loan", value = "Close Loan", group = "LOAN OPERATION")
    public String closeLoan() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        try {
            executeCloseLoan(accountDetails.getLoans());
        } catch (BankingClientException e) {
            return gatewayUtil.formatErrorMessage(e.getMessage());
        }
        return TableFormatter.formatObjectDetails(getTerminal(), accountDetails.getLoans(), "Loans");
    }

    /**
     * Executes the login process.
     *
     * @param username the username
     * @param password the password
     * @return true if login is successful, false otherwise
     * @throws BankingClientException if an error occurs during login
     */
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

    /**
     * Executes the financial report retrieval process.
     *
     * @param accountDetails the account details
     * @return the financial report
     * @throws BankingClientException if an error occurs during the retrieval process
     */
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

    /**
     * Executes the bank account report retrieval process.
     *
     * @param accountDetails the account details
     * @return the bank account report
     * @throws BankingClientException if an error occurs during the retrieval process
     */
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

    /**
     * Executes the open account process.
     *
     * @return the new account details
     * @throws BankingClientException if an error occurs during the open account process
     */
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

    /**
     * Executes the withdrawal process.
     *
     * @param accountDetails the account details
     * @return the transaction response
     * @throws BankingClientException if an error occurs during the withdrawal process
     */
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

    /**
     * Executes the open loan process.
     *
     * @param accountDetails the account details
     * @throws BankingClientException if an error occurs during the open loan process
     */
    private void executeOpenLoan(AccountDetails accountDetails) throws BankingClientException {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getLoanServiceUrl();
            String token = jwtTokenProvider.getToken();
            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token)
                    .accept(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                    .header("Content-Type", jakarta.ws.rs.core.MediaType.APPLICATION_JSON);

            OpenLoanRequestDTO openLoanDto = InputReader.multipleReadInputs(getTerminal(), OpenLoanRequestDTO.class);
            OpenLoanRequest request = new OpenLoanRequest().amount(openLoanDto.getAmount()).interestRate(5.0).termInYears(openLoanDto.getTermInYears()).borrowerName(openLoanDto.getBorrowerName()).idBankAccount(accountDetails.getBankAccount().getId()).idAccount(accountDetails.getId());

            Future<Response> futureResponse = requestBuilder.async().post(Entity.entity(request, jakarta.ws.rs.core.MediaType.APPLICATION_JSON));

            Response response = gatewayUtil.getAsyncResponseNotBlockingPolling(futureResponse);

            log.info("response: {}", response);
            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                LoanDto loanResponse = response.readEntity(LoanDto.class);
                accountSession.updateAccountDetailsFromLoan(loanResponse);
            } else {
                throw new BankingClientException(gatewayUtil.extractErrorMessage(response.readEntity(String.class)));
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new BankingClientException(e.getMessage());
        }
    }

    /**
     * Executes the close loan process.
     *
     * @param loans the list of existing loans
     * @throws BankingClientException if an error occurs during the close loan process
     */
    private void executeCloseLoan(List<Loan> loans) throws BankingClientException {
        List<Loan> existingLoan = loans.stream()
                .filter(l -> l.getLoanStatus().equals("APPROVED")).toList();
        List<SelectorItem<String>> items = new ArrayList<>();

        for (Loan loan : existingLoan) {
            items.add(SelectorItem.of(loan.getId() + " - " + loan.getBorrowerName() + " - " + loan.getAmount(), loan.getId().toString()));
        }

        SingleItemSelector<String, SelectorItem<String>> component = new SingleItemSelector<>(getTerminal(),
                items, "Choose the loan you wish to repay: ", null);
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());
        SingleItemSelector.SingleItemSelectorContext<String, SelectorItem<String>> context = component
                .run(SingleItemSelector.SingleItemSelectorContext.empty());
        String result = context.getResultItem().flatMap(si -> Optional.ofNullable(si.getItem())).get();
        Loan lo = existingLoan.stream().filter(l -> l.getId().toString().equals(result)).findFirst().orElseThrow(() -> new BankingClientException("Selection is Invalid!"));
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getLoanServiceUrl() + "/close-loan/" + result;
            String token = jwtTokenProvider.getToken();
            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token)
                    .accept(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                    .header("Content-Type", jakarta.ws.rs.core.MediaType.APPLICATION_JSON);
            Future<Response> futureResponse = requestBuilder.async().put(Entity.entity("", jakarta.ws.rs.core.MediaType.APPLICATION_JSON));
            Response response = gatewayUtil.getAsyncResponseNotBlockingPolling(futureResponse);

            log.info("response: {}", response);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                lo.setLoanStatus("CLOSED");
                LoanDto loanResponce = new LoanDto().id(lo.getId()).termInYears(lo.getTermInYears()).amount(lo.getAmount()).interestRate(lo.getInterestRate()).borrowerName(lo.getBorrowerName()).loanStatus(LoanDto.LoanStatusEnum.valueOf(lo.getLoanStatus()));
                accountSession.updateAccountDetailsFromLoan(loanResponce);
            } else {
                throw new BankingClientException(gatewayUtil.extractErrorMessage(response.readEntity(String.class)));
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new BankingClientException(e.getMessage());
        }
    }


    /**
     * Checks if the user is authenticated.
     *
     * @return the availability status
     */
    private Availability isAuthenticated() {
        return accountSession.isLoggedIn()
                ? Availability.available()
                : Availability.unavailable("You are not logged in");
    }

    /**
     * Checks if the user is not authenticated.
     *
     * @return the availability status
     */
    private Availability isNotAuthenticated() {
        return !accountSession.isLoggedIn()
                ? Availability.available()
                : Availability.unavailable("You are logged in");
    }

    /**
     * Checks if the user is authenticated and has an open loan.
     *
     * @return the availability status
     */
    private Availability isAuthenticatedAndLoanOpenedExisting() {
        return accountSession.isLoggedIn() && accountSession.getAccountDetails().getLoans().stream().anyMatch(l -> l.getLoanStatus().equals("APPROVED"))
                ? Availability.available()
                : Availability.unavailable("You are not logged in Or you are not Loan in Open Status");
    }
}
