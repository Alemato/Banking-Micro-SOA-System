package it.univaq.sose.bankingserviceclient.commands;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import it.univaq.sose.bankingserviceclient.model.AccountDetails;
import it.univaq.sose.bankingserviceclient.model.dto.OpenLoanRequestDTO;
import it.univaq.sose.bankingserviceclient.security.AccountSession;
import it.univaq.sose.bankingserviceclient.security.JwtTokenProvider;
import it.univaq.sose.bankingserviceclient.util.GatewayUtil;
import it.univaq.sose.bankingserviceclient.util.InputReader;
import it.univaq.sose.bankingserviceclient.util.TableFormatter;
import it.univaq.sose.loanserviceprosumer.model.LoanDto;
import it.univaq.sose.loanserviceprosumer.model.OpenLoanRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.Availability;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.ViewComponent;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.component.view.control.ProgressView;
import org.springframework.shell.component.view.control.Spinner;
import org.springframework.shell.geom.HorizontalAlign;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@ShellComponent
public class LoanCommands extends AbstractShellComponent {
    private final GatewayUtil gatewayUtil;
    private final AccountSession accountSession;
    private final JwtTokenProvider jwtTokenProvider;

    public LoanCommands(GatewayUtil gatewayUtil, AccountSession accountSession, JwtTokenProvider jwtTokenProvider) {
        this.gatewayUtil = gatewayUtil;
        this.accountSession = accountSession;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @ShellMethod(key = "open-loan", value = "Open Loan")
    public String openLoan() {
        AccountDetails accountDetails = accountSession.getAccountDetails();
        executeOpenLoan(accountDetails);
        accountDetails = accountSession.getAccountDetails();
        return TableFormatter.formatObjectDetails(getTerminal(), accountDetails.getLoans(), "Loans");
    }


    @ShellMethod(key = "close-loan", value = "Close Loan")
    public void closeLoan() {
        //AccountDetails accountDetails = accountSession.getAccountDetails();
        //executeCloseLoan();
        //return TableFormatter.formatObjectDetails(accountDetails.getLoans(), "Loans");
        test1();
        int width = getTerminal().getWidth();
        String msg = String.format("%-" + width + "s", "OK");
        getTerminal().writer().write(msg + System.lineSeparator());
        getTerminal().writer().flush();
        //return "OK";
//        getTerminal().writer().write("OK" + System.lineSeparator());
//        getTerminal().writer().flush();
    }

    private void executeOpenLoan(AccountDetails accountDetails) {
        try (Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class)) {
            String uri = gatewayUtil.getLoanServiceUrl() + "/atm-card/" + accountDetails.getId();
            String token = jwtTokenProvider.getToken();
            Invocation.Builder requestBuilder = client.target(uri).request()
                    .header("Authorization", "Bearer " + token)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Content-Type", MediaType.APPLICATION_JSON);

            OpenLoanRequestDTO openLoanDto = InputReader.multipleReadInputs(getTerminal(), OpenLoanRequestDTO.class);
            OpenLoanRequest request = new OpenLoanRequest().amount(openLoanDto.getAmount()).interestRate(5.0).termInYears(openLoanDto.getTermInYears()).borrowerName(openLoanDto.getBorrowerName()).idBankAccount(accountDetails.getBankAccount().getId()).idAccount(accountDetails.getId());

            Future<Response> futureResponse = requestBuilder.async().post(Entity.entity(request, MediaType.APPLICATION_JSON));

            Response response = gatewayUtil.getAsyncResponseNotBlockingPolling(futureResponse);

            log.error("response: {}", response);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                LoanDto loanResponse = response.readEntity(LoanDto.class);
                accountSession.updateAccountDetailsFromLoan(loanResponse);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeCloseLoan() {
        List<SelectorItem<String>> items = new ArrayList<>();
        items.add(SelectorItem.of("key1", "value1"));
        items.add(SelectorItem.of("key2", "value2"));
        SingleItemSelector<String, SelectorItem<String>> component = new SingleItemSelector<>(getTerminal(),
                items, "testSimple", null);
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());
        SingleItemSelector.SingleItemSelectorContext<String, SelectorItem<String>> context = component
                .run(SingleItemSelector.SingleItemSelectorContext.empty());
        String result = context.getResultItem().flatMap(si -> Optional.ofNullable(si.getItem())).get();
        //return "Got value " + result;
    }


    private Availability isAuthenticated() {
        return accountSession.isLoggedIn()
                ? Availability.available()
                : Availability.unavailable("You are not logged in");
    }


    private void test1() {
        HorizontalAlign textAlign = HorizontalAlign.LEFT;
        HorizontalAlign spinnerAlign = HorizontalAlign.LEFT;
        HorizontalAlign percentAlign = HorizontalAlign.LEFT;
        String description = "Query execution in progress. Please wait... We are working for you!";
        long advanceSleep = 200L;
        int logMessagesRate = 5;
        boolean logMessagesSleep = true;


        ArrayList<ProgressView.ProgressViewItem> items = new ArrayList<>();
        items.add(ProgressView.ProgressViewItem.ofText(description.length() + 2, textAlign));
        items.add(ProgressView.ProgressViewItem.ofSpinner(0, spinnerAlign));

        ProgressView.ProgressViewItem[] itemsArray = items.toArray(new ProgressView.ProgressViewItem[0]);
        ProgressView view = new ProgressView(itemsArray);
        view.setSpinner(Spinner.of(Spinner.DOTS12, 65));
        view.setDescription(description);
        view.setRect(0, 0, 20, 1);

        ViewComponent component = getViewComponentBuilder().build(view);
        view.start();

        ViewComponent.ViewComponentRun run = component.runAsync();

        for (int i = 0; i < 101; i++) {
            if (run.isDone()) {
                break;
            }
            sleep(advanceSleep);
            if (run.isDone()) {
                break;
            }
            if (i == 5) {
                view.setDescription("Hang tight, it won't be long now! ;)");
            }

        }

        view.stop();
        run.cancel();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }


}

/*

//        AccountDetails accountDetails = accountSession.getAccountDetails();
//        executeCloseLoan();
//        return TableFormatter.formatObjectDetails(accountDetails.getLoans(), "Loans");




 */