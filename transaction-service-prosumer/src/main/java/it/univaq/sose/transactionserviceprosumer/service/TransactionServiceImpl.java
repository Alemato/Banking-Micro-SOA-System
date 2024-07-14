package it.univaq.sose.transactionserviceprosumer.service;

import it.univaq.sose.accountservice.api.DefaultApi;
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

    @Override
    public void depositMoney(AsyncResponse asyncResponse, BalanceUpdateRequest request) {
        new Thread(() -> {
            it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest balanceUpdateRequest = new it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest();
            balanceUpdateRequest.setBankAccountId(request.getBankAccountId());
            balanceUpdateRequest.setAmount(request.getAmount());
            balanceUpdateRequest.setDescription("Balance update");
            try {
                Thread.sleep(1000); // sleep 1s
                TransactionResponse transactionResponse = bankAccountServiceClient.getBankAccountService().addMoney(balanceUpdateRequest);
                TransactionType transactionType = TransactionType.valueOf(transactionResponse.getTransactionType().value());

                BankAccountTransactionResponse bankAccountTransactionResponse = new BankAccountTransactionResponse(
                        transactionResponse.getBankAccountReceiver().getId(), transactionResponse.getBankAccountReceiver().getAccountId(),
                        transactionResponse.getBankAccountReceiver().getIban(), transactionResponse.getBankAccountReceiver().getBalance());

                ExecuteTransactionResponse executeTransactionResponse = new ExecuteTransactionResponse(
                        transactionResponse.getId(), transactionResponse.getTransactionCode(),
                        transactionResponse.getAmount(), transactionResponse.getDescription(), transactionType,
                        LocalDateTime.parse(transactionResponse.getDate()), null, bankAccountTransactionResponse);

                executeTransactionResponse.setAmount(transactionResponse.getAmount());
                executeTransactionResponse.setId(transactionResponse.getId());
                executeTransactionResponse.setDescription(transactionResponse.getDescription());

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

    @Override
    public void withdrawMoney(AsyncResponse asyncResponse, BalanceUpdateRequest request) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // sleep 1s

                it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest balanceUpdateRequest = new it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest();
                balanceUpdateRequest.setBankAccountId(request.getBankAccountId());
                balanceUpdateRequest.setAmount(request.getAmount());
                balanceUpdateRequest.setDescription("Balance update");
                TransactionResponse transactionResponse = bankAccountServiceClient.getBankAccountService().removeMoney(balanceUpdateRequest);

                log.info("Bank-Account-Service Response for Remove Money: {}", transactionResponse);

                TransactionType transactionType = TransactionType.valueOf(transactionResponse.getTransactionType().value());

                BankAccountTransactionResponse bankAccountTransactionResponse = new BankAccountTransactionResponse(
                        transactionResponse.getBankAccountSender().getId(), transactionResponse.getBankAccountSender().getAccountId(),
                        transactionResponse.getBankAccountSender().getIban(), transactionResponse.getBankAccountSender().getBalance());

                ExecuteTransactionResponse executeTransactionResponse = new ExecuteTransactionResponse(
                        transactionResponse.getId(), transactionResponse.getTransactionCode(),
                        transactionResponse.getAmount(), transactionResponse.getDescription(), transactionType,
                        LocalDateTime.parse(transactionResponse.getDate()), null, bankAccountTransactionResponse);

                executeTransactionResponse.setAmount(transactionResponse.getAmount());
                executeTransactionResponse.setId(transactionResponse.getId());
                executeTransactionResponse.setDescription(transactionResponse.getDescription());

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

    @Override
    public void executeTransfer(AsyncResponse asyncResponse, ExecuteTransferRequest request) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // sleep 1s

                CheckBankAccountTransferRequest checkBankAccountTransferRequest = new CheckBankAccountTransferRequest();
                checkBankAccountTransferRequest.setSenderIban(request.getSenderAccountTransferRequest().getIban());
                checkBankAccountTransferRequest.setReceiverIban(request.getReceiverAccountTransferRequest().getIban());
                checkBankAccountTransferRequest.setAmount(request.getAmount());

                CheckBankAccountRequest checkBankAccountRequest = bankAccountServiceClient.getBankAccountService().checkBankAccountTransfer(checkBankAccountTransferRequest);
                log.info("Bank-Account-Service Response for Check Bank Account Receiver Transfer: {}", checkBankAccountRequest);

                AccountResponse receiverAccount = getAccountDetailsByAccountId(checkBankAccountRequest.getReceiverAccountId());
                log.info("Account-Service Response for Bank Account Details: {}", receiverAccount);

                if (Boolean.FALSE.equals(checkBankAccountReceiverName(request, receiverAccount))) {
                    Response response = Response.serverError().entity(new ErrorResponse("Incorrect recipient name(s).")).build();
                    asyncResponse.resume(response);
                }
                TransferRequest transferRequest = new TransferRequest();
                transferRequest.setSenderAccountId(checkBankAccountRequest.getSenderAccountId());
                transferRequest.setReceiverAccountId(checkBankAccountRequest.getReceiverAccountId());
                transferRequest.setAmount(request.getAmount());
                transferRequest.setDescription(request.getDescription());

                TransactionResponse transactionResponse = bankAccountServiceClient.getBankAccountService().executeTransfer(transferRequest);
                log.info("Bank-Account-Service Response for Check Bank Account Receiver Transfer: {}", checkBankAccountRequest);

                BankAccountTransactionResponse bankAccountSender = new BankAccountTransactionResponse(
                        transactionResponse.getBankAccountSender().getId(), transactionResponse.getBankAccountSender().getAccountId(),
                        transactionResponse.getBankAccountSender().getIban(), transactionResponse.getBankAccountSender().getBalance());

                BankAccountTransactionResponse bankAccountReceiver = new BankAccountTransactionResponse(
                        transactionResponse.getBankAccountReceiver().getId(), transactionResponse.getBankAccountReceiver().getAccountId(),
                        transactionResponse.getBankAccountReceiver().getIban(), null);

                ExecuteTransactionResponse executeTransactionResponse = new ExecuteTransactionResponse(
                        transactionResponse.getId(), transactionResponse.getTransactionCode(),
                        transactionResponse.getAmount(), transactionResponse.getDescription(),
                        TransactionType.valueOf(transactionResponse.getTransactionType().value()),
                        LocalDateTime.parse(transactionResponse.getDate()), bankAccountSender, bankAccountReceiver);

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

                BancomatResponse bancomatResponse = bancomatServiceClient.getBancomatService().getBancomatDetailsByNumber(bancomatTransactionRequest.getNumber());
                log.info("Bancomat-Service Response for Get Bancomat Details: {}", bancomatResponse);

                BankAccountResponse bankAccountResponse = bankAccountServiceClient.getBankAccountService().getBankAccountDetails(bancomatResponse.getAccountId());
                log.info("Bank-Account-Service Response for Get Bank Account Details: {}", bancomatResponse);

                it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest balanceUpdateRequest = new it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest();
                balanceUpdateRequest.setBankAccountId(bankAccountResponse.getId());
                balanceUpdateRequest.setAmount(bancomatTransactionRequest.getAmount());
                balanceUpdateRequest.setDescription("Balance update");
                TransactionResponse bankAccountTransaction = bankAccountServiceClient.getBankAccountService().removeMoney(balanceUpdateRequest);

                log.info("Bank-Account-Service Response for Remove Money: {}", bankAccountTransaction);

                BankAccountTransactionResponse bankAccountTransactionResponse = new BankAccountTransactionResponse(
                        bankAccountTransaction.getBankAccountSender().getId(), bankAccountTransaction.getBankAccountSender().getAccountId(),
                        bankAccountTransaction.getBankAccountSender().getIban(), bankAccountTransaction.getBankAccountSender().getBalance());

                TransactionRequest transactionRequest = new TransactionRequest();
                transactionRequest.setNumber(bancomatTransactionRequest.getNumber());
                transactionRequest.setAmount(bancomatTransactionRequest.getAmount());
                transactionRequest.setDescription(bancomatTransactionRequest.getDescription());

                it.univaq.sose.bancomatservice.webservice.TransactionResponse transactionResponse = bancomatServiceClient.getBancomatService().executeTransaction(transactionRequest);
                log.info("Bancomat-Service Response for ATM Execute Transaction: {}", transactionResponse);
                BancomatTransactionResponse bancomatTransactionResponse = new BancomatTransactionResponse(
                        transactionResponse.getId(), transactionResponse.getTransactionCode(),
                        transactionResponse.getAmount(), transactionResponse.getDescription(),
                        transactionResponse.getDate(), bankAccountTransactionResponse
                );


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

    private AccountResponse getAccountDetailsByAccountId(long idAccount) throws AccountServiceException {
        try {
            DefaultApi client = accountServiceClient.getAccountService();
            return client.getAccount1(idAccount);
        } catch (Exception e) {
            throw new AccountServiceException("Error for Account Service (Get Account)");
        }
    }

    private Boolean checkBankAccountReceiverName(ExecuteTransferRequest executeTransferRequest, AccountResponse senderAccount) {
        return executeTransferRequest.getReceiverAccountTransferRequest().getName().equals(senderAccount.getName()) &&
                executeTransferRequest.getReceiverAccountTransferRequest().getSurname().equals(senderAccount.getSurname());
    }

}
