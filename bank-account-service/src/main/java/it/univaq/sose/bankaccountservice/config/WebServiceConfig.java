package it.univaq.sose.bankaccountservice.config;

import it.univaq.sose.bankaccountservice.webservice.BankAccountService;
import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceConfig {

    private final Bus bus;
    private final BankAccountService bankAccountService;

    public WebServiceConfig(Bus bus, BankAccountService bankAccountService) {
        this.bus = bus;
        this.bankAccountService = bankAccountService;
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, bankAccountService);

        endpoint.publish("/BankAccountService");
        return endpoint;
    }

}
