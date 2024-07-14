package it.univaq.sose.transactionserviceprosumer.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.univaq.sose.bankaccountservice.webservice.BankAccountService;
import it.univaq.sose.bankaccountservice.webservice.BankAccountService_Service;
import it.univaq.sose.transactionserviceprosumer.service.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BankAccountServiceClient {
    private final EurekaClient eurekaClient;
    private BankAccountService_Service bankAccountService;
    private URL lastUrl;

    public BankAccountServiceClient(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        this.bankAccountService = null;
        this.lastUrl = null;
    }

    public BankAccountService getBankAccountService() throws ServiceUnavailableException {
        try {
            List<InstanceInfo> instances = eurekaClient.getInstancesByVipAddress("BANK-ACCOUNT-SERVICE", false);
            if (instances.isEmpty()) {
                log.error("No instances available for BANK-ACCOUNT-SERVICE");
                throw new ServiceUnavailableException("No instances available for BANK-ACCOUNT-SERVICE");
            }

            // Remove the last used instance from the list
            instances.removeIf(instance -> {
                try {
                    return Objects.equals(new URL(instance.getHomePageUrl() + "services/BankAccountService?wsdl"), lastUrl);
                } catch (MalformedURLException e) {
                    log.error("Malformed URL while filtering instances: {}", e.getMessage(), e);
                    return false;
                }
            });

            // If no alternative instances are available, use the last used instance
            if (instances.isEmpty()) {
                log.warn("No alternative instances available for BANCOMAT-SERVICE, using the last used instance");
                if (bankAccountService != null) {
                    return bankAccountService.getBankAccountPort();
                } else {
                    throw new ServiceUnavailableException("BANCOMAT-SERVICE: No alternative instances available and no previously used instance available");
                }
            }

            // Shuffle the list to select a random instance
            Collections.shuffle(instances);
            InstanceInfo instance = instances.get(0);
            String eurekaUrl = instance.getHomePageUrl() + "services/BankAccountService?wsdl";
            URL url = new URL(eurekaUrl);
            bankAccountService = new BankAccountService_Service(url);
            log.info("New Retrieved BankAccountService URL: {}", url);
            lastUrl = url;

            return bankAccountService.getBankAccountPort();
        } catch (MalformedURLException e) {
            log.error("Malformed URL: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Malformed URL: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to retrieve BankAccountService URL: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Failed to retrieve BankAccountService URL: " + e.getMessage());
        }
    }
}
