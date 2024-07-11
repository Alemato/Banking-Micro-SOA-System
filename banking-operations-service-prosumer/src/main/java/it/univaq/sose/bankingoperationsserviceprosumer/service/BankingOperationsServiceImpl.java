package it.univaq.sose.bankingoperationsserviceprosumer.service;

import it.univaq.sose.accountservice.api.DefaultApi;
import it.univaq.sose.accountservice.model.AccountResponse;
import it.univaq.sose.accountservice.model.AddIdBankAccountRequest;
import it.univaq.sose.accountservice.model.OpenBankAccountRequest;
import it.univaq.sose.bankaccountservice.webservice.BankAccountAlradyExistException_Exception;
import it.univaq.sose.bankaccountservice.webservice.BankAccountRequest;
import it.univaq.sose.bankaccountservice.webservice.BankAccountResponse;
import it.univaq.sose.bankaccountservice.webservice.BankAccountService;
import it.univaq.sose.bankingoperationsserviceprosumer.client.AccountServiceClient;
import it.univaq.sose.bankingoperationsserviceprosumer.client.BankAccountServiceClient;
import it.univaq.sose.bankingoperationsserviceprosumer.domain.ErrorResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.domain.OpenAccountRequest;
import it.univaq.sose.bankingoperationsserviceprosumer.domain.OpenAccountResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.util.BankingOperationsUtils;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankingOperationsServiceImpl implements BankingOperationsService {
    private final AccountServiceClient accountServiceClient;
    private final BankAccountServiceClient bankAccountService;

    public BankingOperationsServiceImpl(AccountServiceClient accountServiceClient, BankAccountServiceClient bankAccountService) {
        this.accountServiceClient = accountServiceClient;
        this.bankAccountService = bankAccountService;
    }

    @Override
    public void openAccount(OpenAccountRequest openAccountRequest, AsyncResponse asyncResponse) {
        new Thread(() -> {

            try {
                Thread.sleep(1000);
                WebClient accountClient = accountServiceClient.getWebClientAccountService();
                String locationHeader = null;
                try (Response accountResponse = accountClient.path("/api/account/customer-account").post(
                        new OpenBankAccountRequest()
                                .name(openAccountRequest.getName())
                                .surname(openAccountRequest.getSurname())
                                .username(openAccountRequest.getUsername())
                                .password(openAccountRequest.getPassword())
                                .email(openAccountRequest.getEmail())
                                .phone(openAccountRequest.getPhone())
                )) {
                    log.info("Account-Service Response for Create Account Customer: {}", accountResponse);
                    log.info("Headers from Create Account Customer Response: {}", accountResponse.getHeaders());
                    if (!accountResponse.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
                        throw new AccountServiceException("Error for Account Service (Create Customer Account)");
                    locationHeader = accountResponse.getHeaderString("Location");
                }

                if (locationHeader == null) throw new UrlLocationMalformedException("The URL Location is empty.");
                long idAccount = BankingOperationsUtils.getIdFromUrlLocator(locationHeader);

                OpenAccountResponse openAccountResponse = getOpenAccountResponse(openAccountRequest, idAccount);

                Response response = Response.ok().entity(openAccountResponse).build();
                asyncResponse.resume(response);
            } catch (InterruptedException | UrlLocationMalformedException | BankAccountAlradyExistException_Exception |
                     AccountServiceException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private OpenAccountResponse getOpenAccountResponse(OpenAccountRequest openAccountRequest, long idAccount) throws BankAccountAlradyExistException_Exception, AccountServiceException {
        BankAccountRequest bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setAccountId(idAccount);
        bankAccountRequest.setBalance(openAccountRequest.getBalance());
        BankAccountService bankAccountClient = bankAccountService.getBankAccountService();

        BankAccountResponse bankAccountResponse = bankAccountClient.createBankAccount(bankAccountRequest);
        log.info("Bank-Account-Service Response for Create Bank Account: {}", bankAccountResponse);

        DefaultApi client = accountServiceClient.getAccountService();
        try {
            client.addBankAccount1(idAccount, new AddIdBankAccountRequest().idBankAccount(bankAccountResponse.getId()));
        } catch (Exception e) {
            throw new AccountServiceException("Error for Account Service (Add Bank Account)");
        }
        return getOpenAccountResponse(idAccount, bankAccountResponse);

    }

    private OpenAccountResponse getOpenAccountResponse(long idAccount, BankAccountResponse bankAccountResponse) throws AccountServiceException {
        try {
            DefaultApi client = accountServiceClient.getAccountService();
            AccountResponse account = client.getAccount1(idAccount);
            log.info("Account-Service Response for Get Account: {}", account);
            OpenAccountResponse openAccountResponse = new OpenAccountResponse();
            openAccountResponse.setId(account.getId());
            openAccountResponse.setName(account.getName());
            openAccountResponse.setSurname(account.getSurname());
            openAccountResponse.setUsername(account.getUsername());
            openAccountResponse.setPhone(account.getPhone());
            openAccountResponse.setEmail(account.getEmail());
            openAccountResponse.setIban(bankAccountResponse.getIban());
            openAccountResponse.setBalance(bankAccountResponse.getBalance());
            return openAccountResponse;
        } catch (Exception e) {
            throw new AccountServiceException("Error for Account Service (Get Account)");
        }
    }
}
