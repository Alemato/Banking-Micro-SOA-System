package it.univaq.sose.bankingserviceclient.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.component.view.control.Spinner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class GatewayUtil {

    private final String baseUrl;
    private final int port;
    private final String accountPath;
    private final String bankingOperationPath;
    private final String loanPath;
    private final String financialReportPath;
    private final String transactionPath;

    public GatewayUtil(
            @Value("${gateway.base}") String baseUrl,
            @Value("${gateway.port}") int port,
            @Value("${gateway.paths.account}") String accountPath,
            @Value("${gateway.paths.bankingOperation}") String bankingOperationPath,
            @Value("${gateway.paths.loan}") String loanPath,
            @Value("${gateway.paths.financialReport}") String financialReportPath,
            @Value("${gateway.paths.transaction}") String transactionPath) {
        this.baseUrl = baseUrl;
        this.port = port;
        this.accountPath = accountPath;
        this.bankingOperationPath = bankingOperationPath;
        this.loanPath = loanPath;
        this.financialReportPath = financialReportPath;
        this.transactionPath = transactionPath;
    }

    public String getAccountServiceUrl() {
        return String.format("http://%s:%d%s", baseUrl, port, accountPath);
    }

    public String getBankingOperationServiceUrl() {
        return String.format("http://%s:%d%s", baseUrl, port, bankingOperationPath);
    }

    public String getLoanServiceUrl() {
        return String.format("http://%s:%d%s", baseUrl, port, loanPath);
    }

    public String getFinancialReportServiceUrl() {
        return String.format("http://%s:%d%s", baseUrl, port, financialReportPath);
    }

    public String getTransactionServiceUrl() {
        return String.format("http://%s:%d%s", baseUrl, port, transactionPath);
    }

    public Response getAsyncResponseNotBlockingPolling(Future<Response> responseFuture) throws InterruptedException, ExecutionException {
        Spinner spinner = Spinner.of(Spinner.DOTS12, 100);

        int i = 0;
        int firstLap = 10;
        boolean secondRound = false;

        while (!responseFuture.isDone()) {
            i++;
            String message = secondRound ? "Hang tight, it won't be long now! ;)" : "";
            System.out.print("\r" + spinner.getFrames()[i % spinner.getFrames().length] + " Please wait... We are working for you! " + " " + message);
            if (i >= firstLap) {
                secondRound = true;
            }
            Thread.sleep(spinner.getInterval());
        }
        return responseFuture.get();
    }

    public String extractErrorMessage(String errorDetails) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(errorDetails);
            return rootNode.path("error").asText();
        } catch (Exception e) {
            return "Unknown error";
        }
    }

    public String formatSuccessMessage(String message) {
        return "\n\n************ SUCCESS ***********\n" + message + "\n*******************************\n";
    }

    public String formatErrorMessage(String message) {
        return "\n\n************* ERROR ***********\n" + message + "\n********************************\n";
    }
}
