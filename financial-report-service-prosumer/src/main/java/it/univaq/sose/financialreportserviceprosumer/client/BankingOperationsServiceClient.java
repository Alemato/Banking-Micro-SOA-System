package it.univaq.sose.financialreportserviceprosumer.client;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.univaq.sose.bankingoperationsserviceprosumer.api.BankingOperationsServiceDefaultClient;
import it.univaq.sose.financialreportserviceprosumer.service.ServiceUnavailableException;
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
 * BankingOperationsServiceClient class provides methods to interact with the Banking Operations Service.
 */
@Slf4j
@Service
public class BankingOperationsServiceClient {
    private final EurekaClient eurekaClient;
    private final JacksonJsonProvider jacksonProvider;
    private final AtomicReference<String> lastUrlService = new AtomicReference<>();
    private final List<InstanceInfo> lastInstancesCache = Collections.synchronizedList(new ArrayList<>());

    public BankingOperationsServiceClient(EurekaClient eurekaClient, JacksonJsonProvider jacksonProvider) {
        this.eurekaClient = eurekaClient;
        this.jacksonProvider = jacksonProvider;
    }

    /**
     * Retrieves the URL of the Banking Operations Service from Eureka.
     *
     * @return URL of the Banking Operations Service.
     * @throws ServiceUnavailableException if no instances are available.
     */
    private String getUrlServiceFromEureka() throws ServiceUnavailableException {
        try {
            // Retrieve instances from Eureka or use cached instances if unavailable
            List<InstanceInfo> instances = Optional.ofNullable(eurekaClient.getInstancesByVipAddress("BANKING-OPERATIONS-SERVICE-PROSUMER", false))
                    .filter(list -> !list.isEmpty())
                    .orElseGet(() -> {
                        log.warn("Using cached instances for BANKING-OPERATIONS-SERVICE-PROSUMER");
                        log.warn("lastInstancesCache {}", lastInstancesCache);
                        synchronized (lastInstancesCache) {
                            return new ArrayList<>(lastInstancesCache);  // Return a copy of the cached instances
                        }
                    });

            if (instances == null || instances.isEmpty()) {
                log.error("No instances available for BANKING-OPERATIONS-SERVICE-PROSUMER");
                throw new ServiceUnavailableException("No instances available for BANKING-OPERATIONS-SERVICE-PROSUMER");
            }

            // Update the cache of instances with deep copies
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
                log.warn("No alternative instances available for BANKING-OPERATIONS-SERVICE, using the last used instance");
                if (lastUrl != null) {
                    return lastUrl;
                } else {
                    throw new ServiceUnavailableException("BANKING-OPERATIONS-SERVICE: No alternative instances available and no previously used instance available");
                }
            }

            // Shuffle the list to select a random instance
            Collections.shuffle(instances);
            InstanceInfo instance = instances.get(0);
            String eurekaUrl = instance.getHomePageUrl() + "services";
            log.info("New Retrieved Banking Operations Service URL: {}", eurekaUrl);
            lastUrlService.set(eurekaUrl);

            return eurekaUrl;
        } catch (Exception e) {
            log.error("Failed to retrieve Banking Operations Service URL: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Failed to retrieve Banking Operations Service URL: " + e.getMessage());
        }
    }

    /**
     * Creates a deep copy of a list of InstanceInfo objects.
     *
     * @param instances the original list of InstanceInfo objects
     * @return a deep copy of the list
     */
    private List<InstanceInfo> deepCopyInstanceInfoList(List<InstanceInfo> instances) {
        return instances.stream()
                .map(InstanceInfo::new) // Usa il costruttore di copia
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a BankingOperationsServiceDefaultClient.
     *
     * @return A BankingOperationsServiceDefaultClient.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public BankingOperationsServiceDefaultClient getBankingOperationsServiceClient() throws ServiceUnavailableException {
        return JAXRSClientFactory.create(getUrlServiceFromEureka(), BankingOperationsServiceDefaultClient.class, List.of(jacksonProvider));
    }

    /**
     * Retrieves a JAX-RS Client for the Banking Operations Service.
     *
     * @return A JAX-RS Client.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public Client getClientBankingOperationsService() throws ServiceUnavailableException {
        BankingOperationsServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), BankingOperationsServiceDefaultClient.class, List.of(jacksonProvider));
        return WebClient.client(api);
    }

    /**
     * Retrieves a WebClient for the Banking Operations Service.
     *
     * @return A WebClient.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public WebClient getWebClientBankingOperationsService() throws ServiceUnavailableException {
        BankingOperationsServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), BankingOperationsServiceDefaultClient.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        WebClient webClient = WebClient.fromClient(client);
        webClient.type(MediaType.APPLICATION_JSON);
        return webClient;
    }

    /**
     * Retrieves the ClientConfiguration for the Banking Operations Service.
     *
     * @return The ClientConfiguration.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public ClientConfiguration getClientConfigurationBankingOperationsService() throws ServiceUnavailableException {
        BankingOperationsServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), BankingOperationsServiceDefaultClient.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        return WebClient.getConfig(client);
    }

    /**
     * Retrieves the endpoint URL for the Banking Operations Service.
     *
     * @return The endpoint URL.
     * @throws ServiceUnavailableException if the service is unavailable.
     */
    public String getEndpoint() throws ServiceUnavailableException {
        return getUrlServiceFromEureka();
    }


}
