package it.univaq.sose.bankaccountservice.configuration;

import it.univaq.sose.bankaccountservice.webservice.BankAccountService;
import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.metrics.MetricsFeature;
import org.apache.cxf.metrics.MetricsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Apache CXF.
 */
@Configuration
public class ApacheCXFConfig {

    private final Bus bus;
    private final BankAccountService bankAccountService;
    private final MetricsProvider metricsProvider;

    public ApacheCXFConfig(Bus bus, BankAccountService bankAccountService, MetricsProvider metricsProvider) {
        this.bus = bus;
        this.bankAccountService = bankAccountService;
        this.metricsProvider = metricsProvider;
    }

    /**
     * Bean for logging feature.
     *
     * @return the logging feature with pretty logging enabled
     */
    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true); // Enable pretty logging for more readable logs
        return loggingFeature;
    }

    /**
     * Bean for the web service endpoint.
     *
     * @return the configured endpoint for the bank account service
     */
    @Bean
    public Endpoint endpoint() {
        // Create a new endpoint implementation with the bus, service, and metrics feature
        EndpointImpl endpoint = new EndpointImpl(bus, bankAccountService, null, null, new MetricsFeature[]{
                new MetricsFeature(metricsProvider)
        });
        // Publish the endpoint at the specified URL
        endpoint.publish("/BankAccountService");
        return endpoint;
    }

}
