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
public class FinancialReportServiceClient {
    private final EurekaClient eurekaClient;
    private final JacksonJsonProvider jacksonProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private String lastUrlFinancialReportService;

    public FinancialReportServiceClient(EurekaClient eurekaClient, JacksonJsonProvider jacksonProvider, JwtTokenProvider jwtTokenProvider) {
        this.eurekaClient = eurekaClient;
        this.jacksonProvider = jacksonProvider;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private String getUrlServiceFromEureka() {
        try {
            InstanceInfo instance = eurekaClient.getNextServerFromEureka("FINANCIAL-REPORT-SERVICE-PROSUMER", false);
            String eurekaUrl = instance.getHomePageUrl() + "services";
            if (!Objects.equals(lastUrlFinancialReportService, eurekaUrl)) {
                log.info("New Retrieved FinancialReportService URL: {}", eurekaUrl);
                lastUrlFinancialReportService = eurekaUrl;
            }
            return lastUrlFinancialReportService;
        } catch (Exception e) {
            log.error("Failed to retrieve FinancialReportService URL: {}", e.getMessage(), e);
        }
        return null;
    }

    public DefaultApi getFinancialReportService() {
        return JAXRSClientFactory.create(getUrlServiceFromEureka(), DefaultApi.class, List.of(jacksonProvider));
    }

    public Client getClientFinancialReportService() {
        DefaultApi api = JAXRSClientFactory.create(getUrlServiceFromEureka(), DefaultApi.class, List.of(jacksonProvider));
        return WebClient.client(api);
    }

    public WebClient getWebClientFinancialReportService() {
        DefaultApi api = JAXRSClientFactory.create(getUrlServiceFromEureka(), DefaultApi.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        WebClient webClient = WebClient.fromClient(client);
        webClient.type(MediaType.APPLICATION_JSON);
        return webClient;
    }

    public ClientConfiguration getClientConfigurationFinancialReportService() {
        DefaultApi api = JAXRSClientFactory.create(getUrlServiceFromEureka(), DefaultApi.class, List.of(jacksonProvider));
        Client client = WebClient.client(api);
        return WebClient.getConfig(client);
    }

    public WebClient getWebClientWithAuth() {
        WebClient webClient = getWebClientFinancialReportService();
        String token = jwtTokenProvider.getToken();
        if (token != null) {
            webClient.header("Authorization", "Bearer " + token);
        }
        return webClient;
    }
}
