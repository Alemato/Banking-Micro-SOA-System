package it.univaq.sose.bankingoperationsserviceprosumer.service;

import it.univaq.sose.accountservice.api.DefaultApi;
import it.univaq.sose.accountservice.model.TokenResponse;
import it.univaq.sose.accountservice.model.UserCredentials;
import it.univaq.sose.bankaccountservice.webservice.BankAccountRequest;
import it.univaq.sose.bankaccountservice.webservice.BankAccountResponse;
import it.univaq.sose.bankaccountservice.webservice.BankAccountService;
import it.univaq.sose.bankingoperationsserviceprosumer.client.AccountServiceClient;
import it.univaq.sose.bankingoperationsserviceprosumer.client.BankAccountServiceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class Test {
    private final BankAccountServiceClient bankAccountServiceClient;
    private final AccountServiceClient accountServiceClient;
    private final DefaultApi api;

    public Test(BankAccountServiceClient bankAccountServiceClient, AccountServiceClient accountServiceClient) {
        this.bankAccountServiceClient = bankAccountServiceClient;
        this.accountServiceClient = accountServiceClient;
        this.api = accountServiceClient.getAccountService();
    }


    @GetMapping("/test/{id}")
    public BankAccountResponse runSoap(@PathVariable Long id) {
        BankAccountService bankAccountService = bankAccountServiceClient.getBankAccountService();
        BankAccountRequest req = new BankAccountRequest();
        req.setAccountId(id);
        req.setBalance(new BigDecimal("1000"));
        return bankAccountService.createBankAccount(req);
    }


    @GetMapping("/test-ws")
    public TokenResponse runRs() {
        return api.login1(new UserCredentials().username("admin").password("123456"));

    }

}
