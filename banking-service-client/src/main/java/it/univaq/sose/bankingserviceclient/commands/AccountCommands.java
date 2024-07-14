package it.univaq.sose.bankingserviceclient.commands;

import it.univaq.sose.accountservice.model.AccountResponse;
import it.univaq.sose.bankingserviceclient.client.AccountServiceClient;
import it.univaq.sose.bankingserviceclient.util.InputReader;
import it.univaq.sose.bankingserviceclient.util.TableFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.Table;

@Slf4j
@ShellComponent
public class AccountCommands {

    private final AccountServiceClient accountServiceClient;


    public AccountCommands(AccountServiceClient accountServiceClient) {
        this.accountServiceClient = accountServiceClient;

    }

    @ShellMethod("Login to the banking system")
    public String login() {
        String username = InputReader.readInput("Enter your username: ");
        String password = InputReader.readInput("Enter your password: ");

        boolean success = accountServiceClient.login(username, password);
        return success ? formatSuccessMessage("Login successful. Welcome " + password + "!") : formatErrorMessage("Login failed");
    }

    @ShellMethod(key = "get-account", value = "Get account details")
    public Table getAccount() {
        Long id;
        try {
            id = Long.parseLong(InputReader.readInput("Enter the Account id: "));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input! Please try again");
        }
        WebClient client = accountServiceClient.getWebClientWithAuth();
        String url = client.getBaseURI() + "/api/account/" + id;
        WebClient targetClient = client.replacePath(url);
        AccountResponse response = targetClient.get(AccountResponse.class);

        return TableFormatter.formatObjectDetails(response);
    }

    @ShellMethod(key = "open-bank-account", value = "Get account details")
    public String openBankAccount() {


        String username = InputReader.readInput("Enter your username: ");
        String password = InputReader.readInput("Enter your password: ");

        boolean success = accountServiceClient.login(username, password);
        return success ? formatSuccessMessage("Login successful. Welcome " + password + "!") : formatErrorMessage("Login failed");
    }

    private String formatSuccessMessage(String message) {
        return "\n*** SUCCESS ***\n" + message + "\n****************\n";
    }

    private String formatErrorMessage(String message) {
        return "\n*** ERROR ***\n" + message + "\n****************\n";
    }

}
