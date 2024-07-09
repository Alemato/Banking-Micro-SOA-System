package it.univaq.sose.bankingoperationsserviceprosumer.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.univaq.sose.bankaccountservice.webservice.BankAccountService;
import it.univaq.sose.bankaccountservice.webservice.BankAccountService_Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Slf4j
@Service
public class BankAccountServiceClient {
    private final EurekaClient eurekaClient;
    private BankAccountService_Service bankAccountService;
    private URL lastUrlBankAccountService;

    public BankAccountServiceClient(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        this.bankAccountService = null;
        this.lastUrlBankAccountService = null;
    }

    public BankAccountService getBankAccountService() {
        try {
            InstanceInfo instance = eurekaClient.getNextServerFromEureka("BANK-ACCOUNT-SERVICE", false);
            String eurekaUrl = instance.getHomePageUrl() + "services/BankAccountService?wsdl";
            URL url = new URL(eurekaUrl);
            if (!Objects.equals(lastUrlBankAccountService, url)) {
                bankAccountService = new BankAccountService_Service(url);
                log.info("New Retrieved BankAccountService URL: {}", url);
                lastUrlBankAccountService = url;
            }
            return bankAccountService.getBankAccountPort();
        } catch (MalformedURLException e) {
            log.error("Malformed URL: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Failed to retrieve BankAccountService URL: {}", e.getMessage(), e);
        }
        return null;
    }
}
