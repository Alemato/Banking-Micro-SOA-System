package it.univaq.sose.bankingserviceclient.client;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.univaq.sose.bankingserviceclient.security.JwtTokenProvider;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class LoanServiceClient {
    private final EurekaClient eurekaClient;
    private final JacksonJsonProvider jacksonProvider;
    private String lastUrlService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoanServiceClient(EurekaClient eurekaClient, JacksonJsonProvider jacksonProvider, JwtTokenProvider jwtTokenProvider) {
        this.eurekaClient = eurekaClient;
        this.jacksonProvider = jacksonProvider;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private String getUrlServiceFromEureka() throws ServiceUnavailableException {
        try {
            List<InstanceInfo> instances = eurekaClient.getInstancesByVipAddress("LOAN-SERVICE-PROSUMER", false);
            if (instances.isEmpty()) {
                log.error("No instances available for LOAN-SERVICE");
                throw new ServiceUnavailableException("No instances available for LOAN-SERVICE");
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
                log.warn("No alternative instances available for LOAN-SERVICE, using the last used instance");
                if (lastUrlService != null) {
                    return lastUrlService;
                } else {
                    throw new ServiceUnavailableException("LOAN-SERVICE: No alternative instances available and no previously used instance available");
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

    public LoanServiceDefaultClient getLoanServiceClient() throws ServiceUnavailableException {
        return JAXRSClientFactory.create(getUrlServiceFromEureka(), LoanServiceDefaultClient.class, List.of(jacksonProvider));
    }

    public Client getClientLoanService() throws ServiceUnavailableException {
        LoanServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), LoanServiceDefaultClient.class, List.of(jacksonProvider));
        return WebClient.client(api);
    }

    public WebClient getWebClientLoanService() throws ServiceUnavailableException {
        LoanServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), LoanServiceDefaultClient.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        WebClient webClient = WebClient.fromClient(client);
        webClient.type(MediaType.APPLICATION_JSON);
        return webClient;
    }

    public ClientConfiguration getClientConfigurationLoanService() throws ServiceUnavailableException {
        LoanServiceDefaultClient api = JAXRSClientFactory.create(getUrlServiceFromEureka(), LoanServiceDefaultClient.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        return WebClient.getConfig(client);
    }

    public String getEndpoint() throws ServiceUnavailableException {
        return getUrlServiceFromEureka();
    }

    public WebClient getWebClientWithAuth() throws ServiceUnavailableException {
        WebClient webClient = getWebClientLoanService();
        String token = jwtTokenProvider.getToken();
        if (token != null) {
            webClient.header("Authorization", "Bearer " + token);
        }
        return webClient;
    }
}
