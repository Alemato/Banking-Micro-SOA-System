package it.univaq.sose.loanserviceprosumer.service;

import it.univaq.sose.bankaccountservice.webservice.*;
import it.univaq.sose.loanserviceprosumer.business.LoanManager;
import it.univaq.sose.loanserviceprosumer.client.BankAccountServiceClient;
import it.univaq.sose.loanserviceprosumer.domain.dto.ErrorResponse;
import it.univaq.sose.loanserviceprosumer.domain.dto.LoanDto;
import it.univaq.sose.loanserviceprosumer.domain.dto.OpenLoanRequest;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanManager loanManager;
    private final BankAccountServiceClient bankAccountServiceClient;

    public LoanServiceImpl(LoanManager loanManager, BankAccountServiceClient bankAccountServiceClient) {
        this.loanManager = loanManager;
        this.bankAccountServiceClient = bankAccountServiceClient;
    }

    @Override
    public void openLoan(OpenLoanRequest openLoanRequest, AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);

                BankAccountService client = bankAccountServiceClient.getBankAccountService();
                BalanceUpdateRequest balanceUpdateRequest = new BalanceUpdateRequest();
                balanceUpdateRequest.setAmount(openLoanRequest.getAmount());
                balanceUpdateRequest.setDescription("Open Loan");
                balanceUpdateRequest.setBankAccountId(openLoanRequest.getIdBankAccount());

                LoanDto loanDto = loanManager.openLoan(openLoanRequest);

                client.addMoney(balanceUpdateRequest);

                Response response = Response.status(Response.Status.CREATED).entity(loanDto).build();
                asyncResponse.resume(response);
            } catch (InterruptedException | NotFoundException_Exception e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                /* Trigger ExceptionMapper */
                asyncResponse.resume(e);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void getLoanByIdLoan(long idLoan, AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);

                LoanDto loanDto = loanManager.getLoanById(idLoan);

                Response response = Response.ok().entity(loanDto).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            } catch (LoanNotFoundException e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void getAllLoanByIdBankAccount(long idBankAccount, AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);

                List<LoanDto> loanDtoList = loanManager.getAllLoanByIdBankAccount(idBankAccount);

                Response response = Response.ok().entity(loanDtoList).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void getAllLoanByIdAccount(long idAccount, AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);

                List<LoanDto> loanDtoList = loanManager.getAllLoanByIdAccount(idAccount);

                Response response = Response.ok().entity(loanDtoList).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void closeLoanByIdLoan(long idLoan, AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);

                LoanDto loanDto = loanManager.getLoanById(idLoan);

                BankAccountService client = bankAccountServiceClient.getBankAccountService();
                BankAccountResponse bankAccountResponse = client.getBankAccountDetails(loanDto.getIdAccount());

                if (bankAccountResponse.getBalance().compareTo(loanDto.getAmount()) < 0) {
                    throw new InsufficientFundsException_Exception("Insufficient funds");
                }


                BalanceUpdateRequest balanceUpdateRequest = new BalanceUpdateRequest();
                balanceUpdateRequest.setAmount(loanDto.getAmount());
                balanceUpdateRequest.setDescription("Close Loan");
                balanceUpdateRequest.setBankAccountId(loanDto.getIdBankAccount());
                client = bankAccountServiceClient.getBankAccountService();
                client.removeMoney(balanceUpdateRequest);

                loanManager.closeLoanByIdLoan(loanDto.getId());

                Response response = Response.ok().build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            } catch (LoanNotFoundException | NotFoundException_Exception | InsufficientFundsException_Exception e) {
                Response response = Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            } catch (ServiceUnavailableException e) {
                /* Trigger ExceptionMapper */
                asyncResponse.resume(e);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
