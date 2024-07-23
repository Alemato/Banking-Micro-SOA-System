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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Client service to interact with the BankAccountService registered with Eureka.
 * It uses Apache CXF for creating SOAP clients and integrates with Eureka for service discovery.
 */
@Slf4j
@Service
public class BankAccountServiceClient {
    private final EurekaClient eurekaClient;
    private volatile BankAccountService_Service bankAccountService;
    private final AtomicReference<URL> lastUrl = new AtomicReference<>();
    private final List<InstanceInfo> lastInstancesCache = Collections.synchronizedList(new ArrayList<>());

    public BankAccountServiceClient(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        this.bankAccountService = null;
    }

    /**
     * Retrieves the BankAccountService instance, using Eureka for service discovery.
     *
     * @return the BankAccountService instance
     * @throws ServiceUnavailableException if no service instances are available
     */
    public BankAccountService getBankAccountService() throws ServiceUnavailableException {
        try {
            // Retrieve instances from Eureka or use cached instances if unavailable
            List<InstanceInfo> instances = Optional.ofNullable(eurekaClient.getInstancesByVipAddress("BANK-ACCOUNT-SERVICE", false))
                    .filter(list -> !list.isEmpty())
                    .orElseGet(() -> {
                        log.warn("Using cached instances for BANK-ACCOUNT-SERVICE");
                        log.warn("lastInstancesCache {}", lastInstancesCache);
                        synchronized (lastInstancesCache) {
                            return new ArrayList<>(lastInstancesCache);  // Return a copy of the cached instances
                        }
                    });

            if (instances == null || instances.isEmpty()) {
                log.error("No instances available for BANK-ACCOUNT-SERVICE");
                throw new ServiceUnavailableException("No instances available for BANK-ACCOUNT-SERVICE");
            }

            // Update the instance cache in a synchronized block
            synchronized (lastInstancesCache) {
                lastInstancesCache.clear();
                lastInstancesCache.addAll(instances);
            }

            // Remove the last used instance from the list
            URL lastUrlValue = lastUrl.get();
            if (lastUrlValue != null) {
                instances.removeIf(instance -> {
                    try {
                        return Objects.equals(new URL(instance.getHomePageUrl() + "services/BankAccountService?wsdl"), lastUrlValue);
                    } catch (MalformedURLException e) {
                        log.error("Malformed URL while filtering instances: {}", e.getMessage(), e);
                        return false;
                    }
                });
            }

            // If no alternative instances are available, use the last used instance
            if (instances.isEmpty()) {
                log.warn("No alternative instances available for BANK-ACCOUNT-SERVICE, using the last used instance");
                if (bankAccountService != null) {
                    return bankAccountService.getBankAccountPort();
                } else {
                    throw new ServiceUnavailableException("BANK-ACCOUNT-SERVICE: No alternative instances available and no previously used instance available");
                }
            }

            // Shuffle the list to select a random instance
            Collections.shuffle(instances);
            InstanceInfo instance = instances.get(0);
            String eurekaUrl = instance.getHomePageUrl() + "services/BankAccountService?wsdl";
            URL url = new URL(eurekaUrl);
            bankAccountService = new BankAccountService_Service(url);
            log.info("New Retrieved BankAccountService URL: {}", url);
            lastUrl.set(url);

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
