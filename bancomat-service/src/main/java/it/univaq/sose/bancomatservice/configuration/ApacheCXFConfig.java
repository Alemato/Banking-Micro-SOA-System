package it.univaq.sose.bancomatservice.configuration;

import it.univaq.sose.bancomatservice.webservice.BancomatService;
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
    private final BancomatService bancomatService;
    private final MetricsProvider metricsProvider;

    /**
     * Constructor for ApacheCXFConfig.
     *
     * @param bus             the CXF bus
     * @param bancomatService the Bancomat service
     * @param metricsProvider the metrics provider
     */
    public ApacheCXFConfig(Bus bus, BancomatService bancomatService, MetricsProvider metricsProvider) {
        this.bus = bus;
        this.bancomatService = bancomatService;
        this.metricsProvider = metricsProvider;
    }

    /**
     * Configures the logging feature for CXF.
     *
     * @return the logging feature
     */
    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);  // Enables pretty logging for better readability
        return loggingFeature;
    }

    /**
     * Configures and publishes the endpoint for the Bancomat service.
     *
     * @return the published endpoint
     */
    @Bean
    public Endpoint endpoint() {
        // Create a new EndpointImpl instance with the provided bus, service, and metrics feature
        EndpointImpl endpoint = new EndpointImpl(bus, bancomatService, null, null, new MetricsFeature[]{
                new MetricsFeature(metricsProvider)
        });
        // Publish the endpoint at the specified URL
        endpoint.publish("/BancomatService");
        return endpoint;
    }
}
