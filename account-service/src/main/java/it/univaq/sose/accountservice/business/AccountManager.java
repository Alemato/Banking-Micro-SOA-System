package it.univaq.sose.accountservice.business;

import it.univaq.sose.accountservice.domain.dto.AccountResponse;
import it.univaq.sose.accountservice.domain.dto.AddIdBankAccountRequest;
import it.univaq.sose.accountservice.domain.dto.OpenBankAccountRequest;
import it.univaq.sose.accountservice.domain.dto.UserCredentials;
import it.univaq.sose.accountservice.security.AuthenticationException;
import it.univaq.sose.accountservice.service.NotFoundException;

public interface AccountManager {

    String getJwtToken(UserCredentials userCredentials) throws AuthenticationException;

    AccountResponse createAccountCustomer(OpenBankAccountRequest openBankAccountRequest);

    AccountResponse updateAccountWithIdBankAccount(AddIdBankAccountRequest request) throws NotFoundException;

    AccountResponse createAccountBanker(OpenBankAccountRequest request);

    AccountResponse createAccountAdmin(OpenBankAccountRequest request);

    AccountResponse getAccountByIdAccount(Long idAccount) throws NotFoundException;
}
