package it.univaq.sose.bankingoperationsserviceprosumer.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.univaq.sose.bancomatservice.webservice.BancomatService;
import it.univaq.sose.bancomatservice.webservice.BancomatService_Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Slf4j
@Service
public class BancomatServiceClient {
    private final EurekaClient eurekaClient;
    private BancomatService_Service bancomatService;
    private URL lastUrlBancomatService;

    public BancomatServiceClient(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        this.bancomatService = null;
        this.lastUrlBancomatService = null;
    }

    public BancomatService getBancomatService() {
        try {
            InstanceInfo instance = eurekaClient.getNextServerFromEureka("BANCOMAT-SERVICE", false);
            String eurekaUrl = instance.getHomePageUrl() + "services/BancomatService?wsdl";
            URL url = new URL(eurekaUrl);
            if (!Objects.equals(lastUrlBancomatService, url)) {
                bancomatService = new BancomatService_Service(url);
                log.info("New Retrieved BancomatService URL: {}", url);
                lastUrlBancomatService = url;
            }
            return bancomatService.getBancomatPort();
        } catch (MalformedURLException e) {
            log.error("Malformed URL: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Failed to retrieve BancomatService URL: {}", e.getMessage(), e);
        }
        return null;
    }
}
