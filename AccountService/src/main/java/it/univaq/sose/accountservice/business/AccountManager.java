package it.univaq.sose.accountservice.business;

import it.univaq.sose.accountservice.domain.dto.AccountDto;
import it.univaq.sose.accountservice.domain.dto.OpenBankAccountRequest;
import it.univaq.sose.accountservice.domain.dto.UserCredentials;
import it.univaq.sose.accountservice.security.AuthenticationException;

public interface AccountManager {

    String getJwtToken(UserCredentials userCredentials) throws AuthenticationException;

    AccountDto createAccountCustomer(OpenBankAccountRequest openBankAccountRequest);
}
