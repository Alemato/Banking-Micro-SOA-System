package it.univaq.sose.bankingserviceclient.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}
