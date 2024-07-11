package it.univaq.sose.transactionserviceprosumer.service;

import it.univaq.sose.bankaccountservice.webservice.InsufficientFundsException_Exception;
import it.univaq.sose.bankaccountservice.webservice.NotFoundException_Exception;
import it.univaq.sose.bankaccountservice.webservice.TransactionResponse;
import it.univaq.sose.transactionserviceprosumer.client.AccountServiceClient;
import it.univaq.sose.transactionserviceprosumer.client.BankAccountServiceClient;
import it.univaq.sose.transactionserviceprosumer.domain.TransactionType;
import it.univaq.sose.transactionserviceprosumer.domain.dto.BalanceUpdateRequest;
import it.univaq.sose.transactionserviceprosumer.domain.dto.BankAccountTransactionResponse;
import it.univaq.sose.transactionserviceprosumer.domain.dto.ErrorResponse;
import it.univaq.sose.transactionserviceprosumer.domain.dto.ExecuteTransactionResponse;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.feature.Features;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@Features(features = "org.apache.cxf.ext.logging.LoggingFeature")
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountServiceClient bankAccountServiceClient;
    private final AccountServiceClient accountServiceClient;

    public TransactionServiceImpl(BankAccountServiceClient bankAccountServiceClient, AccountServiceClient accountServiceClient) {
        this.bankAccountServiceClient = bankAccountServiceClient;
        this.accountServiceClient = accountServiceClient;
    }

    @Override
    public void depositMoney(AsyncResponse asyncResponse, BalanceUpdateRequest request) {
        new Thread(() -> {
            it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest balanceUpdateRequest = new it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest();
            balanceUpdateRequest.setAccountId(request.getAccountId());
            balanceUpdateRequest.setAmount(request.getAmount());
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

                Response response = Response.ok(executeTransactionResponse).build();

                asyncResponse.resume(response);
            } catch (NotFoundException_Exception e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void withdrawMoney(AsyncResponse asyncResponse, BalanceUpdateRequest request) {
        new Thread(() -> {

            it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest balanceUpdateRequest = new it.univaq.sose.bankaccountservice.webservice.BalanceUpdateRequest();
            balanceUpdateRequest.setAccountId(request.getAccountId());
            balanceUpdateRequest.setAmount(request.getAmount());
            try {
                Thread.sleep(1000); // sleep 1s
                TransactionResponse transactionResponse = bankAccountServiceClient.getBankAccountService().removeMoney(balanceUpdateRequest);
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

                Response response = Response.ok(executeTransactionResponse).build();

                asyncResponse.resume(response);
            } catch (NotFoundException_Exception e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
            } catch (InsufficientFundsException_Exception e) {
                Response response = Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
