package it.univaq.sose.financialreportserviceprosumer.client;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.univaq.sose.financialreportserviceprosumer.service.ServiceUnavailableException;
import it.univaq.sose.loanserviceprosumer.api.LoanServiceDefaultClient;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * LoanServiceClient class provides methods to interact with the Loan Service.
 */
@Slf4j
@Service
public class LoanServiceClient {
    private final EurekaClient eurekaClient;
    private final JacksonJsonProvider jacksonProvider;
    private final AtomicReference<String> lastUrlService = new AtomicReference<>();
    private final List<InstanceInfo> lastInstancesCache = Collections.synchronizedList(new ArrayList<>());

    public LoanServiceClient(EurekaClient eurekaClient, JacksonJsonProvider jacksonProvider) {
        this.eurekaClient = eurekaClient;
        this.jacksonProvider = jacksonProvider;
    }

    /**
     * Retrieves the URL of the Loan Service from Eureka.
     *
     * @return URL of the Loan Service.
     * @throws ServiceUnavailableException if no instances are available.
     */
    private String getUrlServiceFromEureka() throws ServiceUnavailableException {
        try {
            // Retrieve instances from Eureka or use cached instances if unavailable
            List<InstanceInfo> instances = Optional.ofNullable(eurekaClient.getInstancesByVipAddress("LOAN-SERVICE-PROSUMER", false))
                    .filter(list -> !list.isEmpty())
                    .orElseGet(() -> {
                        log.warn("Using cached instances for LOAN-SERVICE");
                        log.warn("lastInstancesCache {}", lastInstancesCache);
                        synchronized (lastInstancesCache) {
                            return new ArrayList<>(lastInstancesCache);  // Return a copy of the cached instances
                        }
                    });

            if (instances == null || instances.isEmpty()) {
                log.error("No instances available for LOAN-SERVICE-PROSUMER");
                throw new ServiceUnavailableException("No instances available for LOAN-SERVICE-PROSUMER");
            }

            // Update the cache of instances synchronously
            synchronized (lastInstancesCache) {
                lastInstancesCache.clear();
                lastInstancesCache.addAll(deepCopyInstanceInfoList(instances));
            }

            // Remove the last used instance from the list
            String lastUrl = lastUrlService.get();
            if (lastUrl != null) {
                instances.removeIf(instance -> {
                    try {
                        return Objects.equals(new URL(instance.getHomePageUrl() + "services"), new URL(lastUrl));
                    } catch (MalformedURLException e) {
                        log.error("Malformed URL while filtering instances: {}", e.getMessage(), e);
                        return false;
                    }
                });
            }

            // If no alternative instances are available, use the last used instance
            if (instances.isEmpty()) {
                log.warn("No alternative instances available for LOAN-SERVICE, using the last used instance");
                if (lastUrl != null) {
                    return lastUrl;
                } else {
                    throw new ServiceUnavailableException("LOAN-SERVICE: No alternative instances available and no previously used instance available");
                }
            }

            // Shuffle the list to select a random instance
            Collections.shuffle(instances);
            InstanceInfo instance = instances.get(0);
            String eurekaUrl = instance.getHomePageUrl() + "services";
            log.info("New Retrieved Loan Service URL: {}", eurekaUrl);
            lastUrlService.set(eurekaUrl);

            return eurekaUrl;
        } catch (Exception e) {
            log.error("Failed to retrieve Loan Service URL: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Failed to retrieve Loan Service URL: " + e.getMessage());
        }
    }

    /**
     * Creates a deep copy of the InstanceInfo list.
     *
     * @param instances The list of InstanceInfo to be copied.
     * @return A deep copy of the InstanceInfo list.
     */
    private List<InstanceInfo> deepCopyInstanceInfoList(List<InstanceInfo> instances) {
        return instances.stream()
                .map(InstanceInfo::new) // Usa il costruttore di copia
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a LoanServiceDefaultClient.
     *
     * @return A LoanServiceDefaultClient.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public LoanServiceDefaultClient getLoanServiceClient() throws ServiceUnavailableException {
        return JAXRSClientFactory.create(getUrlServiceFromEureka(), LoanServiceDefaultClient.class, List.of(jacksonProvider));
    }

    /**
     * Retrieves a JAX-RS Client for the Loan Service.
     *
     * @return A JAX-RS Client.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public Client getClientLoanService() throws ServiceUnavailableException {
        LoanServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), LoanServiceDefaultClient.class, List.of(jacksonProvider));
        return WebClient.client(api);
    }

    /**
     * Retrieves a WebClient for the Loan Service.
     *
     * @return A WebClient.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public WebClient getWebClientLoanService() throws ServiceUnavailableException {
        LoanServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), LoanServiceDefaultClient.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        WebClient webClient = WebClient.fromClient(client);
        webClient.type(MediaType.APPLICATION_JSON);
        return webClient;
    }

    /**
     * Retrieves the ClientConfiguration for the Loan Service.
     *
     * @return The ClientConfiguration.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public ClientConfiguration getClientConfigurationLoanService() throws ServiceUnavailableException {
        LoanServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), LoanServiceDefaultClient.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        return WebClient.getConfig(client);
    }

    /**
     * Retrieves the endpoint URL for the Loan Service.
     *
     * @return The endpoint URL.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public String getEndpoint() throws ServiceUnavailableException {
        return getUrlServiceFromEureka();
    }
}
