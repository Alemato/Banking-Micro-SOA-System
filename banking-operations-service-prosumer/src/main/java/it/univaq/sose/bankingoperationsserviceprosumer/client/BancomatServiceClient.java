package it.univaq.sose.bankingoperationsserviceprosumer.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.univaq.sose.bancomatservice.webservice.BancomatService;
import it.univaq.sose.bancomatservice.webservice.BancomatService_Service;
import it.univaq.sose.bankingoperationsserviceprosumer.service.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BancomatServiceClient {
    private final EurekaClient eurekaClient;
    private BancomatService_Service bancomatService;
    private URL lastUrl;

    public BancomatServiceClient(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        this.bancomatService = null;
        this.lastUrl = null;
    }

    public BancomatService getBancomatService() throws ServiceUnavailableException {
        try {
            List<InstanceInfo> instances = eurekaClient.getInstancesByVipAddress("BANCOMAT-SERVICE", false);
            if (instances.isEmpty()) {
                log.error("No instances available for BANCOMAT-SERVICE");
                throw new ServiceUnavailableException("No instances available for BANCOMAT-SERVICE");
            }

            // Remove the last used instance from the list
            instances.removeIf(instance -> {
                try {
                    return Objects.equals(new URL(instance.getHomePageUrl() + "services/BancomatService?wsdl"), lastUrl);
                } catch (MalformedURLException e) {
                    log.error("Malformed URL while filtering instances: {}", e.getMessage(), e);
                    return false;
                }
            });

            // If no alternative instances are available, use the last used instance
            if (instances.isEmpty()) {
                log.warn("No alternative instances available for BANCOMAT-SERVICE, using the last used instance");
                if (bancomatService != null) {
                    return bancomatService.getBancomatPort();
                } else {
                    throw new ServiceUnavailableException("BANCOMAT-SERVICE: No alternative instances available and no previously used instance available");
                }
            }

            // Shuffle the list to select a random instance
            Collections.shuffle(instances);
            InstanceInfo instance = instances.get(0);
            String eurekaUrl = instance.getHomePageUrl() + "services/BancomatService?wsdl";
            URL url = new URL(eurekaUrl);
            bancomatService = new BancomatService_Service(url);
            log.info("New Retrieved BankAccountService URL: {}", url);
            lastUrl = url;

            return bancomatService.getBancomatPort();
        } catch (MalformedURLException e) {
            log.error("Malformed URL: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Malformed URL: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to retrieve BankAccountService URL: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Failed to retrieve BankAccountService URL: " + e.getMessage());
        }
    }
}
