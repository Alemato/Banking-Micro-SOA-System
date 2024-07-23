package it.univaq.sose.financialreportserviceprosumer.client.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.sose.financialreportserviceprosumer.service.FinancialServiceException;
import it.univaq.sose.loanserviceprosumer.model.LoanDto;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Callback class for handling asynchronous responses from the loan service.
 */
@Slf4j
@Getter
public class AllLoanCallBack implements InvocationCallback<Response> {
    private List<LoanDto> loanListResponses;
    private boolean hasError = false;
    private Throwable throwable;

    /**
     * Called when the response is successfully received.
     *
     * @param response The response from the loan service.
     */
    @Override
    public void completed(Response response) {
        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            try {
                String jsonResponse = response.readEntity(String.class);
                ObjectMapper objectMapper = new ObjectMapper();
                loanListResponses = objectMapper.readValue(jsonResponse, new TypeReference<List<LoanDto>>() {
                });
            } catch (ProcessingException | IllegalStateException e) {
                log.error("LoanCallback: Response content of the message cannot be mapped to List<LoanDto>");
                handleException(new FinancialServiceException("Response content of the message cannot be mapped to List<LoanDto>", e));
            } catch (JsonMappingException e) {
                log.error("LoanCallback: JsonMappingException");
                handleException(new FinancialServiceException("JsonMappingException"));
            } catch (JsonProcessingException e) {
                log.error("LoanCallback: JsonProcessingException");
                handleException(new FinancialServiceException("JsonProcessingException"));
            }
        } else {
            log.error("LoanCallback: AllLoanResponse returned with status: {}", response.getStatus());
            handleException(new FinancialServiceException("AllLoanResponse returned with status: " + response.getStatus()));
        }
    }

    /**
     * Called when the response fails to be received.
     *
     * @param throwable The exception thrown during the request.
     */
    @Override
    public void failed(Throwable throwable) {
        log.error("AllLoan Error on Callback", throwable);
        handleException(new FinancialServiceException("AllLoan Error on Callback", throwable));
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
