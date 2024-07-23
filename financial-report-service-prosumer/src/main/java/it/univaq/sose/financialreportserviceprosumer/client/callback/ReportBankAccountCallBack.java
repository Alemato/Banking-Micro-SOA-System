package it.univaq.sose.financialreportserviceprosumer.client.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.sose.bankingoperationsserviceprosumer.model.ReportBankAccountResponse;
import it.univaq.sose.financialreportserviceprosumer.service.FinancialServiceException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Callback class for handling asynchronous responses from the banking operations service.
 */
@Getter
@Slf4j
public class ReportBankAccountCallBack implements InvocationCallback<Response> {
    private ReportBankAccountResponse reportBankAccountResponse;
    private boolean hasError = false;
    private Throwable throwable;

    /**
     * Called when the response is successfully received.
     *
     * @param response The response from the banking operations service.
     */
    @Override
    public void completed(Response response) {
        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            try {
                String messageFromTheServer = response.readEntity(String.class);
                ObjectMapper objectMapper = new ObjectMapper();
                reportBankAccountResponse = objectMapper.readValue(messageFromTheServer, ReportBankAccountResponse.class);
            } catch (ProcessingException | IllegalStateException e) {
                log.error("ReportBankAccount: Response content of the message cannot be Report Bank Account Type");
                handleException(new FinancialServiceException("Response content of the message cannot be Report Bank Account Type"));
            } catch (JsonMappingException e) {
                log.error("ReportBankAccount: JsonMappingException");
                handleException(new FinancialServiceException("JsonMappingException"));
            } catch (JsonProcessingException e) {
                log.error("ReportBankAccount: JsonProcessingException");
                handleException(new FinancialServiceException("JsonProcessingException"));
            }
        } else {
            log.error("ReportBankAccountResponse returned with status: {}", response.getStatus());
            handleException(new FinancialServiceException("ReportBankAccountResponse returned with status: " + response.getStatus()));
        }
    }

    /**
     * Called when the response fails to be received.
     *
     * @param throwable The exception thrown during the request.
     */
    @Override
    public void failed(Throwable throwable) {
        log.error("ReportBankAccount Error on Callback", throwable);
        handleException(new FinancialServiceException("ReportBankAccount Error on Callback", throwable));
    }

    /**
     * Handles exceptions by setting the error state and rethrowing the exception.
     *
     * @param e The exception to be handled.
     */
    private void handleException(FinancialServiceException e) {
        this.hasError = true;
        this.throwable = e;
        throw e; // Rilancia l'eccezione
    }

}
