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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BankingOperationsServiceClient {
    private final EurekaClient eurekaClient;
    private final JacksonJsonProvider jacksonProvider;
    private String lastUrlService;

    public BankingOperationsServiceClient(EurekaClient eurekaClient, JacksonJsonProvider jacksonProvider) {
        this.eurekaClient = eurekaClient;
        this.jacksonProvider = jacksonProvider;
    }

    private String getUrlServiceFromEureka() throws ServiceUnavailableException {
        try {
            List<InstanceInfo> instances = eurekaClient.getInstancesByVipAddress("BANKING-OPERATIONS-SERVICE-PROSUMER", false);
            if (instances.isEmpty()) {
                log.error("No instances available for BANKING-OPERATIONS-SERVICE");
                throw new ServiceUnavailableException("No instances available for BANKING-OPERATIONS-SERVICE");
            }

            // Rimuove l'ultima istanza utilizzata dalla lista
            if (lastUrlService != null) {
                instances.removeIf(instance -> {
                    try {
                        return Objects.equals(new URL(instance.getHomePageUrl() + "services"), new URL(lastUrlService));
                    } catch (MalformedURLException e) {
                        log.error("Malformed URL while filtering instances: {}", e.getMessage(), e);
                        return false;
                    }
                });
            }

            // Se non ci sono istanze alternative disponibili, utilizza l'ultima istanza utilizzata
            if (instances.isEmpty()) {
                log.warn("No alternative instances available for BANKING-OPERATIONS-SERVICE, using the last used instance");
                if (lastUrlService != null) {
                    return lastUrlService;
                } else {
                    throw new ServiceUnavailableException("BANKING-OPERATIONS-SERVICE: No alternative instances available and no previously used instance available");
                }
            }

            // Mescola la lista per selezionare un'istanza casuale
            Collections.shuffle(instances);
            InstanceInfo instance = instances.get(0);
            String eurekaUrl = instance.getHomePageUrl() + "services";
            log.info("New Retrieved Banking Operations Service URL: {}", eurekaUrl);
            lastUrlService = eurekaUrl;

            return lastUrlService;
        } catch (Exception e) {
            log.error("Failed to retrieve Banking Operations Service URL: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Failed to retrieve Banking Operations Service URL: " + e.getMessage());
        }
    }

    public BankingOperationsServiceDefaultClient getBankingOperationsServiceClient() throws ServiceUnavailableException {
        return JAXRSClientFactory.create(getUrlServiceFromEureka(), BankingOperationsServiceDefaultClient.class, List.of(jacksonProvider));
    }

    public Client getClientBankingOperationsService() throws ServiceUnavailableException {
        BankingOperationsServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), BankingOperationsServiceDefaultClient.class, List.of(jacksonProvider));
        return WebClient.client(api);
    }

    public WebClient getWebClientAccountService() throws ServiceUnavailableException {
        BankingOperationsServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), BankingOperationsServiceDefaultClient.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        WebClient webClient = WebClient.fromClient(client);
        webClient.type(MediaType.APPLICATION_JSON);
        return webClient;
    }

    public ClientConfiguration getClientConfigurationAccountService() throws ServiceUnavailableException {
        BankingOperationsServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), BankingOperationsServiceDefaultClient.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        return WebClient.getConfig(client);
    }

    public String getEndpoint() throws ServiceUnavailableException {
        return getUrlServiceFromEureka();
    }
}
