package it.univaq.sose.accountservice.business.impl;

import it.univaq.sose.accountservice.business.AccountManager;
import it.univaq.sose.accountservice.domain.Account;
import it.univaq.sose.accountservice.domain.Role;
import it.univaq.sose.accountservice.domain.dto.AccountDto;
import it.univaq.sose.accountservice.domain.dto.OpenBankAccountRequest;
import it.univaq.sose.accountservice.domain.dto.UserCredentials;
import it.univaq.sose.accountservice.repository.AccountRepository;
import it.univaq.sose.accountservice.security.AuthenticationException;
import it.univaq.sose.accountservice.security.JWTGenerator;
import it.univaq.sose.accountservice.security.PasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountManagerImpl implements AccountManager {
    private final AccountRepository accountRepository;
    private final PasswordService passwordService;

    public AccountManagerImpl(AccountRepository accountRepository, PasswordService passwordService) {
        this.accountRepository = accountRepository;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional(readOnly = true)
    public String getJwtToken(UserCredentials userCredentials) throws AuthenticationException {
        Account account = accountRepository.findByUsername(userCredentials.getUsername()).orElseThrow(() -> new AuthenticationException("Username o Password errata. Riprovare."));
        if (!passwordService.checkPassword(userCredentials.getPassword(), account.getPassword())) {
            throw new AuthenticationException("Username o Password errata. Riprovare.");
        }
        return JWTGenerator.createJwtToken(account.getUsername(), account.getRole());
    }

    @Override
    @Transactional
    public AccountDto createAccountCustomer(OpenBankAccountRequest request) {
        Account account = new Account();
        account.setName(request.getName());
        account.setSurname(request.getSurname());
        account.setUsername(request.getUsername());
        account.setPassword(passwordService.hashPassword(request.getPassword()));
        account.setRole(Role.CUSTOMER);
        account = accountRepository.save(account);
        return new AccountDto(account.getId(), account.getCreateDate(), account.getUpdateDate(), account.getName(), account.getSurname(), account.getUsername(), account.getRole(), account.getIdBankAccount());
    }
}
