package it.univaq.sose.transactionserviceprosumer.service;

import it.univaq.sose.accountservice.api.AccountServiceDefaultClient;
import it.univaq.sose.accountservice.model.AccountResponse;
import it.univaq.sose.bancomatservice.webservice.BancomatResponse;
import it.univaq.sose.bancomatservice.webservice.ExpiredBancomatException_Exception;
import it.univaq.sose.bancomatservice.webservice.TransactionRequest;
import it.univaq.sose.bankaccountservice.webservice.*;
import it.univaq.sose.transactionserviceprosumer.client.AccountServiceClient;
import it.univaq.sose.transactionserviceprosumer.client.BancomatServiceClient;
import it.univaq.sose.transactionserviceprosumer.client.BankAccountServiceClient;
import it.univaq.sose.transactionserviceprosumer.domain.TransactionType;
import it.univaq.sose.transactionserviceprosumer.domain.dto.BalanceUpdateRequest;
import it.univaq.sose.transactionserviceprosumer.domain.dto.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementation of the TransactionService interface, handling transaction operations.
 */
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountServiceClient bankAccountServiceClient;
    private final BancomatServiceClient bancomatServiceClient;
    private final AccountServiceClient accountServiceClient;

    public TransactionServiceImpl(BankAccountServiceClient bankAccountServiceClient, BancomatServiceClient bancomatServiceClient, AccountServiceClient accountServiceClient) {
        this.bankAccountServiceClient = bankAccountServiceClient;
        this.bancomatServiceClient = bancomatServiceClient;
        this.accountServiceClient = accountServiceClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void depositMoney(AsyncResponse asyncResponse, BalanceUpdateRequest request) {
        new Thread(() -> {
            it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest balanceUpdateRequest = new it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest();
            balanceUpdateRequest.setBankAccountId(request.getBankAccountId());
            balanceUpdateRequest.setAmount(request.getAmount());
            balanceUpdateRequest.setDescription(request.getDescription());
            try {
                Thread.sleep(1000); // sleep 1s

                // Call the bank account service to add money
                TransactionResponse transactionResponse = bankAccountServiceClient.getBankAccountService().addMoney(balanceUpdateRequest);
                // Map transaction type
                TransactionType transactionType = TransactionType.valueOf(transactionResponse.getTransactionType().value());

                // Create response for the bank account transaction
                BankAccountTransactionResponse bankAccountTransactionResponse = new BankAccountTransactionResponse(
                        transactionResponse.getBankAccountReceiver().getId(), transactionResponse.getBankAccountReceiver().getAccountId(),
                        transactionResponse.getBankAccountReceiver().getIban(), transactionResponse.getBankAccountReceiver().getBalance());

                // Create the transaction response
                ExecuteTransactionResponse executeTransactionResponse = new ExecuteTransactionResponse(
                        transactionResponse.getId(), transactionResponse.getTransactionCode(),
                        transactionResponse.getAmount(), transactionResponse.getDescription(), transactionType,
                        LocalDateTime.parse(transactionResponse.getDate()), null, bankAccountTransactionResponse);

                executeTransactionResponse.setAmount(transactionResponse.getAmount());
                executeTransactionResponse.setId(transactionResponse.getId());
                executeTransactionResponse.setDescription(transactionResponse.getDescription());

                // Build the response
                Response response = Response.status(Response.Status.CREATED).entity(executeTransactionResponse).build();
                asyncResponse.resume(response);
            } catch (NotFoundException_Exception e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                asyncResponse.resume(e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void withdrawMoney(AsyncResponse asyncResponse, BalanceUpdateRequest request) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // sleep 1s

                // Prepare balance update request
                it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest balanceUpdateRequest = new it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest();
                balanceUpdateRequest.setBankAccountId(request.getBankAccountId());
                balanceUpdateRequest.setAmount(request.getAmount());
                balanceUpdateRequest.setDescription(request.getDescription());

                // Call the bank account service to remove money
                TransactionResponse transactionResponse = bankAccountServiceClient.getBankAccountService().removeMoney(balanceUpdateRequest);

                log.info("Bank-Account-Service Response for Remove Money: {}", transactionResponse);

                // Map transaction type
                TransactionType transactionType = TransactionType.valueOf(transactionResponse.getTransactionType().value());

                // Create response for the bank account transaction
                BankAccountTransactionResponse bankAccountTransactionResponse = new BankAccountTransactionResponse(
                        transactionResponse.getBankAccountSender().getId(), transactionResponse.getBankAccountSender().getAccountId(),
                        transactionResponse.getBankAccountSender().getIban(), transactionResponse.getBankAccountSender().getBalance());

                // Create the transaction response
                ExecuteTransactionResponse executeTransactionResponse = new ExecuteTransactionResponse(
                        transactionResponse.getId(), transactionResponse.getTransactionCode(),
                        transactionResponse.getAmount(), transactionResponse.getDescription(), transactionType,
                        LocalDateTime.parse(transactionResponse.getDate()), null, bankAccountTransactionResponse);

                executeTransactionResponse.setAmount(transactionResponse.getAmount());
                executeTransactionResponse.setId(transactionResponse.getId());
                executeTransactionResponse.setDescription(transactionResponse.getDescription());

                // Build the response
                Response response = Response.status(Response.Status.CREATED).entity(executeTransactionResponse).build();
                asyncResponse.resume(response);
            } catch (NotFoundException_Exception e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (InsufficientFundsException_Exception e) {
                Response response = Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (InterruptedException e) {
                Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                asyncResponse.resume(e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeTransfer(AsyncResponse asyncResponse, ExecuteTransferRequest request) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // sleep 1s

                // Prepare the check bank account transfer request
                CheckBankAccountTransferRequest checkBankAccountTransferRequest = new CheckBankAccountTransferRequest();
                checkBankAccountTransferRequest.setSenderIban(request.getSenderAccountTransferRequest().getIban());
                checkBankAccountTransferRequest.setReceiverIban(request.getReceiverAccountTransferRequest().getIban());
                checkBankAccountTransferRequest.setAmount(request.getAmount());

                // Call the bank account service to check the transfer
                CheckBankAccountRequest checkBankAccountRequest = bankAccountServiceClient.getBankAccountService().checkBankAccountTransfer(checkBankAccountTransferRequest);
                log.info("Bank-Account-Service Response for Check Bank Account Receiver Transfer: {}", checkBankAccountRequest);

                // Get receiver account details
                AccountResponse receiverAccount = getAccountDetailsByAccountId(checkBankAccountRequest.getReceiverAccountId());
                log.info("Account-Service Response for Bank Account Details: {}", receiverAccount);

                // Check if the recipient's name matches
                if (Boolean.FALSE.equals(checkBankAccountReceiverName(request, receiverAccount))) {
                    Response response = Response.serverError().entity(new ErrorResponse("Incorrect recipient name(s).")).build();
                    asyncResponse.resume(response);
                }

                // Prepare the transfer request
                TransferRequest transferRequest = new TransferRequest();
                transferRequest.setSenderAccountId(checkBankAccountRequest.getSenderAccountId());
                transferRequest.setReceiverAccountId(checkBankAccountRequest.getReceiverAccountId());
                transferRequest.setAmount(request.getAmount());
                transferRequest.setDescription(request.getDescription());

                // Call the bank account service to execute the transfer
                TransactionResponse transactionResponse = bankAccountServiceClient.getBankAccountService().executeTransfer(transferRequest);
                log.info("Bank-Account-Service Response for Check Bank Account Receiver Transfer: {}", checkBankAccountRequest);

                // Create response for the sender and receiver bank accounts
                BankAccountTransactionResponse bankAccountSender = new BankAccountTransactionResponse(
                        transactionResponse.getBankAccountSender().getId(), transactionResponse.getBankAccountSender().getAccountId(),
                        transactionResponse.getBankAccountSender().getIban(), transactionResponse.getBankAccountSender().getBalance());

                BankAccountTransactionResponse bankAccountReceiver = new BankAccountTransactionResponse(
                        transactionResponse.getBankAccountReceiver().getId(), transactionResponse.getBankAccountReceiver().getAccountId(),
                        transactionResponse.getBankAccountReceiver().getIban(), null);

                // Create the transaction response
                ExecuteTransactionResponse executeTransactionResponse = new ExecuteTransactionResponse(
                        transactionResponse.getId(), transactionResponse.getTransactionCode(),
                        transactionResponse.getAmount(), transactionResponse.getDescription(),
                        TransactionType.valueOf(transactionResponse.getTransactionType().value()),
                        LocalDateTime.parse(transactionResponse.getDate()), bankAccountSender, bankAccountReceiver);

                // Build the response
                Response response = Response.status(Response.Status.CREATED).entity(executeTransactionResponse).build();
                asyncResponse.resume(response);

            } catch (InterruptedException | AccountServiceException | NotFoundException_Exception |
                     InsufficientFundsException_Exception e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                asyncResponse.resume(e);
                Thread.currentThread().interrupt();
            }
        }).start();

    }

    @Override
    public void executeAtmPayment(AsyncResponse asyncResponse, BancomatTransactionRequest bancomatTransactionRequest) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // sleep 1s

                // Get bancomat details by number
                BancomatResponse bancomatResponse = bancomatServiceClient.getBancomatService().getBancomatDetailsByNumber(bancomatTransactionRequest.getNumber());
                log.info("Bancomat-Service Response for Get Bancomat Details: {}", bancomatResponse);

                // Get bank account details
                BankAccountResponse bankAccountResponse = bankAccountServiceClient.getBankAccountService().getBankAccountDetails(bancomatResponse.getAccountId());
                log.info("Bank-Account-Service Response for Get Bank Account Details: {}", bancomatResponse);

                // Prepare balance update request
                it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest balanceUpdateRequest = new it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest();
                balanceUpdateRequest.setBankAccountId(bankAccountResponse.getId());
                balanceUpdateRequest.setAmount(bancomatTransactionRequest.getAmount());
                balanceUpdateRequest.setDescription("Balance update");

                // Call the bank account service to remove money
                TransactionResponse bankAccountTransaction = bankAccountServiceClient.getBankAccountService().removeMoney(balanceUpdateRequest);
                log.info("Bank-Account-Service Response for Remove Money: {}", bankAccountTransaction);

                // Create response for the bank account transaction
                BankAccountTransactionResponse bankAccountTransactionResponse = new BankAccountTransactionResponse(
                        bankAccountTransaction.getBankAccountSender().getId(), bankAccountTransaction.getBankAccountSender().getAccountId(),
                        bankAccountTransaction.getBankAccountSender().getIban(), bankAccountTransaction.getBankAccountSender().getBalance());

                // Prepare bancomat transaction request
                TransactionRequest transactionRequest = new TransactionRequest();
                transactionRequest.setNumber(bancomatTransactionRequest.getNumber());
                transactionRequest.setAmount(bancomatTransactionRequest.getAmount());
                transactionRequest.setDescription(bancomatTransactionRequest.getDescription());

                // Call the bancomat service to execute the transaction
                it.univaq.sose.bancomatservice.webservice.BancomatTransactionResponse transactionResponse = bancomatServiceClient.getBancomatService().executeTransaction(transactionRequest);
                log.info("Bancomat-Service Response for ATM Execute Transaction: {}", transactionResponse);

                // Create the bancomat transaction response
                BancomatTransactionResponse bancomatTransactionResponse = new BancomatTransactionResponse(
                        transactionResponse.getId(), transactionResponse.getTransactionCode(),
                        transactionResponse.getAmount(), transactionResponse.getDescription(),
                        transactionResponse.getDate(), bankAccountTransactionResponse
                );

                // Build the response
                Response response = Response.status(Response.Status.CREATED).entity(bancomatTransactionResponse).build();
                asyncResponse.resume(response);

            } catch (InterruptedException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (it.univaq.sose.bancomatservice.webservice.NotFoundException_Exception |
                     NotFoundException_Exception e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (ExpiredBancomatException_Exception e) {
                Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (InsufficientFundsException_Exception e) {
                Response response = Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                asyncResponse.resume(e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Get account details by account ID.
     *
     * @param idAccount the account ID
     * @return the account response
     * @throws AccountServiceException if there is an error retrieving the account
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
     * Check if the recipient's name matches.
     *
     * @param executeTransferRequest the execute transfer request
     * @param senderAccount          the receiver account
     * @return true if the recipient's name matches, false otherwise
     */
    private Boolean checkBankAccountReceiverName(ExecuteTransferRequest executeTransferRequest, AccountResponse senderAccount) {
        return executeTransferRequest.getReceiverAccountTransferRequest().getName().equals(senderAccount.getName()) &&
                executeTransferRequest.getReceiverAccountTransferRequest().getSurname().equals(senderAccount.getSurname());
    }

}
