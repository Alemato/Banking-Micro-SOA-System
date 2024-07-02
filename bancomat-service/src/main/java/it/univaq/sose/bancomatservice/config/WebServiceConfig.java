package it.univaq.sose.bancomatservice.config;

import it.univaq.sose.bancomatservice.webservice.BancomatService;
import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceConfig {

    private final Bus bus;
    private final BancomatService bancomatService;

    public WebServiceConfig(Bus bus, BancomatService bancomatService) {
        this.bus = bus;
        this.bancomatService = bancomatService;
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, bancomatService);

        endpoint.publish("/BancomatService");
        return endpoint;
    }
}
