package it.univaq.sose.accountservice.service;

import it.univaq.sose.accountservice.business.AccountManager;
import it.univaq.sose.accountservice.domain.dto.*;
import it.univaq.sose.accountservice.security.AuthenticationException;
import jakarta.ws.rs.core.Response;
import org.apache.cxf.feature.Features;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Implementation of the AccountService interface.
 */
@Service
@Features(features = "org.apache.cxf.ext.logging.LoggingFeature")
public class AccountServiceImpl implements AccountService {
    private final AccountManager accountManager;

    @Value("${cxf.path}")
    private String cxfPath;

    public AccountServiceImpl(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response login(UserCredentials credentials) {
        try {
            // Generate JWT token
            String token = accountManager.getJwtToken(credentials);
            return Response.ok(new TokenResponse(token)).build();
        } catch (AuthenticationException e) {
            // Return unauthorized response if authentication fails
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean checkTokenResponse(TokenResponse token) {
        // Check the validity of the provided token
        return accountManager.checkJwtToken(token.getToken());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response openAccountBanker(OpenBankAccountRequest request) {
        try {
            return generateResponseLocationFromAccountResponse(accountManager.createAccountBanker(request));
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response openAccountAdmin(OpenBankAccountRequest request) {
        try {
            return generateResponseLocationFromAccountResponse(accountManager.createAccountAdmin(request));
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response openAccountCustomer(OpenBankAccountRequest request) {
        try {
            return generateResponseLocationFromAccountResponse(accountManager.createAccountCustomer(request));
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addBankAccount(long id, AddIdBankAccountRequest request) {
        try {
            return Response.ok(accountManager.updateAccountWithIdBankAccount(id, request)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAccount(long id) {
        try {
            return Response.ok(accountManager.getAccountByIdAccount(id)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * Generates a response with the location header from the account response.
     *
     * @param account the account response
     * @return the response with the location header
     */
    private Response generateResponseLocationFromAccountResponse(AccountResponse account) {
        // Build the URI for the created account
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(cxfPath + "/api/account/{id}")
                .buildAndExpand(account.getId())
                .toUri();
        return Response.created(location).build();
    }
}
