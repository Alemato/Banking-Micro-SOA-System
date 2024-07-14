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

@Getter
@Slf4j
public class ReportBankAccountCallBack implements InvocationCallback<Response> {
    private ReportBankAccountResponse reportBankAccountResponse;
    private boolean hasError = false;
    private Throwable throwable;

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

    @Override
    public void failed(Throwable throwable) {
        log.error("ReportBankAccount Error on Callback", throwable);
        handleException(new FinancialServiceException("ReportBankAccount Error on Callback", throwable));
    }

    private void handleException(FinancialServiceException e) {
        this.hasError = true;
        this.throwable = e;
        throw e; // Rilancia l'eccezione
    }

}
