package it.univaq.sose.financialreportserviceprosumer.service;

import it.univaq.sose.bancomatservice.webservice.*;
import it.univaq.sose.bankingoperationsserviceprosumer.model.ReportBankAccountResponse;
import it.univaq.sose.financialreportserviceprosumer.client.BancomatServiceClient;
import it.univaq.sose.financialreportserviceprosumer.client.BankingOperationsServiceClient;
import it.univaq.sose.financialreportserviceprosumer.client.LoanServiceClient;
import it.univaq.sose.financialreportserviceprosumer.client.callback.AllLoanCallBack;
import it.univaq.sose.financialreportserviceprosumer.client.callback.ReportBankAccountCallBack;
import it.univaq.sose.financialreportserviceprosumer.domain.ErrorResponse;
import it.univaq.sose.financialreportserviceprosumer.domain.FinancialReportResponse;
import it.univaq.sose.loanserviceprosumer.model.LoanDto;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.xml.ws.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Service
public class FinancialReportServiceImpl implements FinancialReportService {
    private final BancomatServiceClient bancomatServiceClient;
    private final BankingOperationsServiceClient bankingServiceClient;
    private final LoanServiceClient loanServiceClient;

    public FinancialReportServiceImpl(BancomatServiceClient bancomatServiceClient, BankingOperationsServiceClient bankingServiceClient, LoanServiceClient loanServiceClient) {
        this.bancomatServiceClient = bancomatServiceClient;
        this.bankingServiceClient = bankingServiceClient;
        this.loanServiceClient = loanServiceClient;
    }

    @Override
    public void getFinancialReportByIdAccount(long idAccount, AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                ReportBankAccountCallBack bankingServiceCallBack = new ReportBankAccountCallBack();
                AllLoanCallBack loanServiceCallBack = new AllLoanCallBack();

                BancomatService bancomatService = bancomatServiceClient.getBancomatService();

                GetBancomatDetails requestBancomat = new GetBancomatDetails();
                requestBancomat.setAccountId(idAccount);

                GetBancomatTransactions requestBancomatTransactions = new GetBancomatTransactions();
                requestBancomatTransactions.setAccountId(idAccount);

                Response<GetBancomatDetailsResponse> bancomatResponse = bancomatService.getBancomatDetailsAsync(requestBancomat);
                Response<GetBancomatTransactionsResponse> bancomatTransactionsResponse = bancomatService.getBancomatTransactionsAsync(requestBancomatTransactions);

                try (Client client = ClientBuilder.newClient(); Client client1 = ClientBuilder.newClient()) {
                    Future<jakarta.ws.rs.core.Response> bankingResponse = client.target(bankingServiceClient.getEndpoint() + "/api/bank/report-bank-account-by-account/" + idAccount).request().async().get(bankingServiceCallBack);
                    Future<jakarta.ws.rs.core.Response> loanResponse = client1.target(loanServiceClient.getEndpoint() + "/api/loan/account/" + idAccount).request().async().get(loanServiceCallBack);

                    log.info("Response form: BANCOMAT: {}, BANCOMAT1: {}, BANKING-OPERATION: {}, LOAN: {}", bancomatResponse.isDone(), bancomatTransactionsResponse.isDone(), bankingResponse.isDone(), loanResponse.isDone());
                    Thread.sleep(600);

                    while (!bancomatResponse.isDone() || !bancomatTransactionsResponse.isDone() || !bankingResponse.isDone() || !loanResponse.isDone()) {
                        Thread.sleep(100);
                        log.info("Response form: BANCOMAT: {}, BANCOMAT1: {}, BANKING-OPERATION: {}, LOAN: {}", bancomatResponse.isDone(), bancomatTransactionsResponse.isDone(), bankingResponse.isDone(), loanResponse.isDone());

                        if (bankingServiceCallBack.isHasError()) {
                            throw (FinancialServiceException) bankingServiceCallBack.getThrowable();
                        }
                        if (loanServiceCallBack.isHasError()) {
                            throw (FinancialServiceException) loanServiceCallBack.getThrowable();
                        }
                    }

                    BancomatResponse bancomatReply = bancomatResponse.get().getGetBancomatDetailsResponse();
                    List<TransactionResponse> bancomatTransactionsReply = bancomatTransactionsResponse.get().getGetBancomatTransactionsResponse();
                    ReportBankAccountResponse reportBankAccountReplay = bankingServiceCallBack.getReportBankAccountResponse();
                    List<LoanDto> allLoanReplay = loanServiceCallBack.getLoanListResponses();

                    log.info("RESULT BancomatResponse IS {}", bancomatReply);
                    log.info("RESULT BancomatTransactionsResponse IS {}", bancomatTransactionsReply);
                    log.info("RESULT ReportBankAccountResponse IS {}", reportBankAccountReplay);
                    log.info("RESULT AllLoan IS {}", allLoanReplay);

                    FinancialReportResponse financialReportResponse = new FinancialReportResponse(reportBankAccountReplay.getAccount(), reportBankAccountReplay.getBankAccount(), reportBankAccountReplay.getTransactions(), bancomatReply, bancomatTransactionsReply, allLoanReplay);
                    jakarta.ws.rs.core.Response response = jakarta.ws.rs.core.Response.ok(financialReportResponse).build();
                    asyncResponse.resume(response);
                } catch (Exception e) {
                    jakarta.ws.rs.core.Response response = jakarta.ws.rs.core.Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                    asyncResponse.resume(response);
                    /* Clean up whatever needs to be handled before interrupting  */
                    Thread.currentThread().interrupt();
                }
            } catch (ServiceUnavailableException e) {
                /* Trigger ExceptionMapper */
                asyncResponse.resume(e);
            }
        }).start();
    }
}
