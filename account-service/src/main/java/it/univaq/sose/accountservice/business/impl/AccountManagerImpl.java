package it.univaq.sose.accountservice.business.impl;

import it.univaq.sose.accountservice.business.AccountManager;
import it.univaq.sose.accountservice.domain.Account;
import it.univaq.sose.accountservice.domain.Role;
import it.univaq.sose.accountservice.domain.dto.AccountResponse;
import it.univaq.sose.accountservice.domain.dto.AddIdBankAccountRequest;
import it.univaq.sose.accountservice.domain.dto.OpenBankAccountRequest;
import it.univaq.sose.accountservice.domain.dto.UserCredentials;
import it.univaq.sose.accountservice.repository.AccountRepository;
import it.univaq.sose.accountservice.security.AuthenticationException;
import it.univaq.sose.accountservice.security.JWTGenerator;
import it.univaq.sose.accountservice.security.PasswordService;
import it.univaq.sose.accountservice.service.NotFoundException;
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
    public AccountResponse createAccountCustomer(OpenBankAccountRequest request) {
        Account account = new Account();
        account.setName(request.getName());
        account.setSurname(request.getSurname());
        account.setUsername(request.getUsername());
        account.setPassword(passwordService.hashPassword(request.getPassword()));
        account.setRole(Role.CUSTOMER);
        account = accountRepository.save(account);
        return new AccountResponse(account.getId(), account.getName(), account.getSurname(), account.getUsername(), account.getRole(), account.getIdBankAccount(), account.getUpdateDate(), account.getCreateDate());
    }

    @Override
    @Transactional
    public AccountResponse updateAccountWithIdBankAccount(AddIdBankAccountRequest request) throws NotFoundException {
        Account account = accountRepository.findById(request.getIdAccount()).orElseThrow(() -> new NotFoundException("Account not found"));
        account.setIdBankAccount(request.getIdBankAccount());
        account = accountRepository.save(account);
        return new AccountResponse(account.getId(), account.getName(), account.getSurname(), account.getUsername(), account.getRole(), account.getIdBankAccount(), account.getUpdateDate(), account.getCreateDate());
    }

    @Override
    @Transactional
    public AccountResponse createAccountBanker(OpenBankAccountRequest request) {
        Account account = new Account();
        account.setName(request.getName());
        account.setSurname(request.getSurname());
        account.setUsername(request.getUsername());
        account.setPassword(passwordService.hashPassword(request.getPassword()));
        account.setRole(Role.BANKER);
        account = accountRepository.save(account);
        return new AccountResponse(account.getId(), account.getName(), account.getSurname(), account.getUsername(), account.getRole(), account.getIdBankAccount(), account.getUpdateDate(), account.getCreateDate());
    }

    @Override
    @Transactional
    public AccountResponse createAccountAdmin(OpenBankAccountRequest request) {
        Account account = new Account();
        account.setName(request.getName());
        account.setSurname(request.getSurname());
        account.setUsername(request.getUsername());
        account.setPassword(passwordService.hashPassword(request.getPassword()));
        account.setRole(Role.ADMIN);
        account = accountRepository.save(account);
        return new AccountResponse(account.getId(), account.getName(), account.getSurname(), account.getUsername(), account.getRole(), account.getIdBankAccount(), account.getUpdateDate(), account.getCreateDate());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountByIdAccount(Long idAccount) throws NotFoundException {
        Account account = accountRepository.findById(idAccount).orElseThrow(() -> new NotFoundException("Account not found"));
        return new AccountResponse(account.getId(), account.getName(), account.getSurname(), account.getUsername(), account.getRole(), account.getIdBankAccount(), account.getUpdateDate(), account.getCreateDate());
    }
}