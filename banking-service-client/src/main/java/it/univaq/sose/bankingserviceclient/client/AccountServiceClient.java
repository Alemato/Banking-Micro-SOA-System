package it.univaq.sose.bankingserviceclient.client;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.univaq.sose.accountservice.api.DefaultApi;
import it.univaq.sose.bankingserviceclient.security.JwtTokenProvider;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AccountServiceClient {
    private final EurekaClient eurekaClient;
    private final JacksonJsonProvider jacksonProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private String lastUrlAccountService;

    public AccountServiceClient(EurekaClient eurekaClient, JacksonJsonProvider jacksonProvider, JwtTokenProvider jwtTokenProvider) {
        this.eurekaClient = eurekaClient;
        this.jacksonProvider = jacksonProvider;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private String getUrlServiceFromEureka() {
        try {
            InstanceInfo instance = eurekaClient.getNextServerFromEureka("ACCOUNT-SERVICE", false);
            String eurekaUrl = instance.getHomePageUrl() + "services";
            if (!Objects.equals(lastUrlAccountService, eurekaUrl)) {
                log.info("New Retrieved AccountService URL: {}", eurekaUrl);
                lastUrlAccountService = eurekaUrl;
            }
            return lastUrlAccountService;
        } catch (Exception e) {
            log.error("Failed to retrieve AccountService URL: {}", e.getMessage(), e);
        }
        return null;
    }

    public DefaultApi getAccountService() {
        return JAXRSClientFactory.create(getUrlServiceFromEureka(), DefaultApi.class, List.of(jacksonProvider));
    }

    public Client getClientAccountService() {
        DefaultApi api = JAXRSClientFactory.create(getUrlServiceFromEureka(), DefaultApi.class, List.of(jacksonProvider));
        return WebClient.client(api);
    }

    public WebClient getWebClientAccountService() {
        DefaultApi api = JAXRSClientFactory.create(getUrlServiceFromEureka(), DefaultApi.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        WebClient webClient = WebClient.fromClient(client);
        webClient.type(MediaType.APPLICATION_JSON);
        return webClient;
    }

    public ClientConfiguration getClientConfigurationAccountService() {
        DefaultApi api = JAXRSClientFactory.create(getUrlServiceFromEureka(), DefaultApi.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        return WebClient.getConfig(client);
    }

    public WebClient getWebClientWithAuth() {
        WebClient webClient = getWebClientAccountService();
        String token = jwtTokenProvider.getToken();
        if (token != null) {
            webClient.header("Authorization", "Bearer " + token);
        }
        return webClient;
    }

}
