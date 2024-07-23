package it.univaq.sose.bankingoperationsserviceprosumer.service;

import it.univaq.sose.accountservice.api.AccountServiceDefaultClient;
import it.univaq.sose.accountservice.model.AccountResponse;
import it.univaq.sose.accountservice.model.AddIdBankAccountRequest;
import it.univaq.sose.accountservice.model.OpenBankAccountRequest;
import it.univaq.sose.bancomatservice.webservice.*;
import it.univaq.sose.bankaccountservice.webservice.NotFoundException_Exception;
import it.univaq.sose.bankaccountservice.webservice.*;
import it.univaq.sose.bankingoperationsserviceprosumer.client.AccountServiceClient;
import it.univaq.sose.bankingoperationsserviceprosumer.client.BancomatServiceClient;
import it.univaq.sose.bankingoperationsserviceprosumer.client.BankAccountServiceClient;
import it.univaq.sose.bankingoperationsserviceprosumer.domain.CreateBancomatResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.domain.*;
import it.univaq.sose.bankingoperationsserviceprosumer.util.BankingOperationsUtils;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the BankingOperationsService interface.
 * This class handles the actual logic for opening accounts, retrieving reports, and managing ATM cards.
 */
@Slf4j
@Service
public class BankingOperationsServiceImpl implements BankingOperationsService {
    private final AccountServiceClient accountServiceClient;
    private final BankAccountServiceClient bankAccountService;
    private final BancomatServiceClient bancomatServiceClient;

    public BankingOperationsServiceImpl(AccountServiceClient accountServiceClient, BankAccountServiceClient bankAccountService, BancomatServiceClient bancomatServiceClient) {
        this.accountServiceClient = accountServiceClient;
        this.bankAccountService = bankAccountService;
        this.bancomatServiceClient = bancomatServiceClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openAccount(OpenAccountRequest openAccountRequest, AsyncResponse asyncResponse) {
        new Thread(() -> {

            try {
                // Simulate processing delay
                Thread.sleep(1000);

                // Create account
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

                // Get account response
                OpenAccountResponse openAccountResponse = getOpenAccountResponse(openAccountRequest, idAccount);

                // Respond with created status
                Response response = Response.status(Response.Status.CREATED).entity(openAccountResponse).build();
                asyncResponse.resume(response);
            } catch (InterruptedException | UrlLocationMalformedException | BankAccountAlradyExistException_Exception |
                     AccountServiceException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                /* Trigger ExceptionMapper */
                asyncResponse.resume(e);
                Thread.currentThread().interrupt();
            } catch (it.univaq.sose.bancomatservice.webservice.NotFoundException_Exception e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (BancomatAlreadyExistingException_Exception e) {
                Response response = Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getReportBankAccountFromIdAccount(long idAccount, AsyncResponse asyncResponse) {
        new Thread(() -> {

            try {
                // Simulate processing delay
                Thread.sleep(1000);

                // Get account details
                AccountResponse account = getAccountDetailsByAccountId(idAccount);
                log.info("Account-Service Response for Bank Account Details: {}", account);

                // Get bank account details
                BankAccountService bankAccountClient = bankAccountService.getBankAccountService();
                BankAccountResponse bankAccountResponse = bankAccountClient.getBankAccountDetails(account.getId());
                log.info("Bank-Account-Service Response for Bank Account Details: {}", bankAccountResponse);

                // Get bank account transactions
                List<TransactionResponse> transactionResponseList = bankAccountClient.getBankAccountTransactions(account.getId());
                log.info("Bank-Account-Service Response for Bank Account Transactions: {}", transactionResponseList);

                // Create report response
                ReportBankAccountResponse report = getReportBankAccountResponse(account, bankAccountResponse, transactionResponseList);

                // Respond with OK status
                Response response = Response.ok().entity(report).build();
                asyncResponse.resume(response);
            } catch (InterruptedException | AccountServiceException | NotFoundException_Exception e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                /* Trigger ExceptionMapper */
                asyncResponse.resume(e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void requestAtmCard(AsyncResponse asyncResponse, long accountId) {
        new Thread(() -> {

            try {
                // Simulate processing delay
                Thread.sleep(1000); // sleep 1s

                // Get bank account details
                BankAccountResponse bankAccountResponse = bankAccountService.getBankAccountService().getBankAccountDetails(accountId);
                log.info("Bank-Account-Service Response for Get Bank Account: {}", bankAccountResponse);

                // Create ATM card request
                BancomatRequest bancomatRequest = new BancomatRequest();
                bancomatRequest.setAccountId(accountId);
                bancomatRequest.setBankAccountId(bankAccountResponse.getId());

                // Create ATM card
                BancomatResponse bancomatResponse = bancomatServiceClient.getBancomatService().createBancomat(bancomatRequest);
                log.info("Bancomat-Service Response for Create Bancomat: {}", bancomatResponse);

                // Create response
                CreateBancomatResponse createBancomatResponse = new CreateBancomatResponse(
                        bancomatResponse.getId(), bancomatResponse.getNumber(),
                        bancomatResponse.getCvv(), bancomatResponse.getExpiryDate()
                );

                // Respond with created status
                Response response = Response.status(Response.Status.CREATED).entity(createBancomatResponse).build();
                asyncResponse.resume(response);

            } catch (InterruptedException e) {
                Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (BancomatAlreadyExistingException_Exception e) {
                Response response = Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (it.univaq.sose.bancomatservice.webservice.NotFoundException_Exception |
                     NotFoundException_Exception e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                /* Trigger ExceptionMapper */
                asyncResponse.resume(e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getAtmCard(AsyncResponse asyncResponse, long accountId) {
        new Thread(() -> {

            try {
                // Simulate processing delay
                Thread.sleep(1000); // sleep 1s

                // Get ATM card details
                GetBancomatDetails request = new GetBancomatDetails();
                request.setAccountId(accountId);
                GetBancomatDetailsResponse getBancomatDetailsResponse = bancomatServiceClient.getBancomatService().getBancomatDetails(request);
                BancomatResponse bancomatResponse = getBancomatDetailsResponse.getGetBancomatDetailsResponse();
                log.info("Bancomat-Service Response for Get Bancomat Details: {}", bancomatResponse);

                // Create response
                CreateBancomatResponse createBancomatResponse = new CreateBancomatResponse(
                        bancomatResponse.getId(), bancomatResponse.getNumber(),
                        bancomatResponse.getCvv(), bancomatResponse.getExpiryDate()
                );

                // Respond with created status
                Response response = Response.status(Response.Status.CREATED).entity(createBancomatResponse).build();
                asyncResponse.resume(response);

            } catch (InterruptedException e) {
                Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (it.univaq.sose.bancomatservice.webservice.NotFoundException_Exception |
                     BancomatException_Exception e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                /* Trigger ExceptionMapper */
                asyncResponse.resume(e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Helper method to create a ReportBankAccountResponse object.
     *
     * @param account                 The account response from the account service.
     * @param bankAccountResponse     The bank account response from the bank account service.
     * @param transactionResponseList The list of transaction responses.
     * @return The ReportBankAccountResponse object.
     */
    private static ReportBankAccountResponse getReportBankAccountResponse(AccountResponse account, BankAccountResponse bankAccountResponse, List<TransactionResponse> transactionResponseList) {
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setId(account.getId());
        accountDetails.setName(account.getName());
        accountDetails.setSurname(account.getSurname());
        accountDetails.setUsername(account.getUsername());
        accountDetails.setEmail(account.getEmail());
        accountDetails.setPhone(account.getPhone());

        BankAccountDetails bankAccountDetails = new BankAccountDetails();
        bankAccountDetails.setId(bankAccountResponse.getId());
        bankAccountDetails.setAccountId(bankAccountResponse.getAccountId());
        bankAccountDetails.setIban(bankAccountResponse.getIban());
        bankAccountDetails.setBalance(bankAccountResponse.getBalance());

        ReportBankAccountResponse report = new ReportBankAccountResponse();
        report.setAccount(accountDetails);
        report.setBankAccount(bankAccountDetails);
        report.setTransactions(transactionResponseList);
        return report;
    }

    /**
     * Helper method to get account details by account ID.
     *
     * @param idAccount The account ID.
     * @return The account response.
     * @throws AccountServiceException If there is an error retrieving the account details.
     */
    private AccountResponse getAccountDetailsByAccountId(long idAccount) throws AccountServiceException {
        try {
            AccountServiceDefaultClient client = accountServiceClient.getAccountService();
            return client.getAccount1(idAccount);
        } catch (Exception e) {
            throw new AccountServiceException("Error for Account Service (Get Account)");
        }

    }

    /**
     * Helper method to create an OpenAccountResponse object.
     *
     * @param openAccountRequest The open account request.
     * @param idAccount          The account ID.
     * @return The OpenAccountResponse object.
     * @throws BankAccountAlradyExistException_Exception If the bank account already exists.
     * @throws AccountServiceException                   If there is an error with the account service.
     * @throws ServiceUnavailableException               If a service is unavailable.
     * @throws it.univaq.sose.bancomatservice.webservice.NotFoundException_Exception If the bancomat service cannot find the resource.
     * @throws BancomatAlreadyExistingException_Exception If the bancomat already exists.
     */
    private OpenAccountResponse getOpenAccountResponse(OpenAccountRequest openAccountRequest, long idAccount) throws BankAccountAlradyExistException_Exception, AccountServiceException, ServiceUnavailableException, it.univaq.sose.bancomatservice.webservice.NotFoundException_Exception, BancomatAlreadyExistingException_Exception {
        BankAccountRequest bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setAccountId(idAccount);
        bankAccountRequest.setBalance(openAccountRequest.getBalance());
        BankAccountService bankAccountClient = bankAccountService.getBankAccountService();
        BancomatService bancomatService = bancomatServiceClient.getBancomatService();

        // Create bank account
        BankAccountResponse bankAccountResponse = bankAccountClient.createBankAccount(bankAccountRequest);
        log.info("Bank-Account-Service Response for Create Bank Account: {}", bankAccountResponse);

        // Create ATM card
        BancomatRequest bancomatRequest = new BancomatRequest();
        bancomatRequest.setAccountId(bankAccountResponse.getAccountId());
        bancomatRequest.setBankAccountId(bankAccountResponse.getId());

        BancomatResponse bancomatResponse = bancomatService.createBancomat(bancomatRequest);

        AccountServiceDefaultClient client = accountServiceClient.getAccountService();
        try {
            client.addBankAccount1(idAccount, new AddIdBankAccountRequest().idBankAccount(bankAccountResponse.getId()));
        } catch (Exception e) {
            throw new AccountServiceException("Error for Account Service (Add Bank Account)");
        }
        return getOpenAccountResponse(idAccount, bankAccountResponse, bancomatResponse);
    }


    /**
     * Helper method to create an OpenAccountResponse object.
     *
     * @param idAccount          The account ID.
     * @param bankAccountResponse The bank account response.
     * @param bancomatResponse    The bancomat response.
     * @return The OpenAccountResponse object.
     * @throws AccountServiceException If there is an error with the account service.
     */
    private OpenAccountResponse getOpenAccountResponse(long idAccount, BankAccountResponse bankAccountResponse, BancomatResponse bancomatResponse) throws AccountServiceException {
        try {
            AccountServiceDefaultClient client = accountServiceClient.getAccountService();
            AccountResponse account = client.getAccount1(idAccount);
            log.info("Account-Service Response for Get Account: {}", account);
            OpenAccountResponse openAccountResponse = new OpenAccountResponse();
            openAccountResponse.setId(account.getId());
            openAccountResponse.setName(account.getName());
            openAccountResponse.setSurname(account.getSurname());
            openAccountResponse.setUsername(account.getUsername());
            openAccountResponse.setPhone(account.getPhone());
            openAccountResponse.setEmail(account.getEmail());

            openAccountResponse.setBankAccountId(bankAccountResponse.getId());
            openAccountResponse.setIban(bankAccountResponse.getIban());
            openAccountResponse.setBalance(bankAccountResponse.getBalance());

            openAccountResponse.setBancomatId(bancomatResponse.getId());
            openAccountResponse.setBancomatNumber(bancomatResponse.getNumber());
            openAccountResponse.setBancomatCvv(bancomatResponse.getCvv());
            openAccountResponse.setBancomatExpiryDate(bancomatResponse.getExpiryDate());
            return openAccountResponse;
        } catch (Exception e) {
            throw new AccountServiceException("Error for Account Service (Get Account)");
        }
    }
}
