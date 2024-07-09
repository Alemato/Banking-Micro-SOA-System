package it.univaq.sose.bankingoperationsserviceprosumer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import it.univaq.sose.bankaccountservice.webservice.BankAccountService;
import it.univaq.sose.bankaccountservice.webservice.BankAccountService_Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

@Configuration
public class ApacheCXFConfig {
    private final EurekaClient eurekaClient;

    public ApacheCXFConfig(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Bean
    public JacksonJsonProvider jsonProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new JacksonJsonProvider(objectMapper);
    }

    @Bean
    public BankAccountService_Service bankAccountService() throws MalformedURLException {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("BANK-ACCOUNT-SERVICE", false);
        String serviceUrl = instance.getHomePageUrl() + "services/BankAccountService?wsdl";
        return new BankAccountService_Service(new URL(serviceUrl));
    }

    @Bean
    public BankAccountService bankAccountPort(BankAccountService_Service bankAccountService) {
        return bankAccountService.getBankAccountPort();
    }
}
