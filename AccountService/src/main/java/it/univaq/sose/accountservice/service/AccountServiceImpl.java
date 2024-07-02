package it.univaq.sose.accountservice.service;

import it.univaq.sose.accountservice.business.AccountManager;
import it.univaq.sose.accountservice.domain.dto.*;
import it.univaq.sose.accountservice.security.AuthenticationException;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountManager accountManager;

    public AccountServiceImpl(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public Response login(UserCredentials credentials) {
        try {
            String token = accountManager.getJwtToken(credentials);
            return Response.ok(new TokenResponse(token)).build();
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Override
    public AccountResponse openAccountBanker(OpenBankAccountRequest request) {
        return accountManager.createAccountBanker(request);
    }

    @Override
    public AccountResponse openAccountAdmin(OpenBankAccountRequest request) {
        return accountManager.createAccountAdmin(request);
    }

    @Override
    public AccountResponse openAccountCustomer(OpenBankAccountRequest request) {
        return accountManager.createAccountCustomer(request);
    }

    @Override
    public Response addBankAccount(AddIdBankAccountRequest request) {
        try {
            return Response.ok(accountManager.updateAccountWithIdBankAccount(request)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Override
    public Response getAccount(long id) {
        try {
            return Response.ok(accountManager.getAccountByIdAccount(id)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}
