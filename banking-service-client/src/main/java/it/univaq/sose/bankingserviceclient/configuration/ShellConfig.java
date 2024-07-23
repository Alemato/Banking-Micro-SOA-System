package it.univaq.sose.bankingserviceclient.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

/**
 * Configuration class for the Shell and JSON provider.
 */
@Configuration
public class ShellConfig {

    /**
     * Configures a JacksonJsonProvider with custom settings.
     *
     * @return a configured JacksonJsonProvider
     */
    @Bean
    public JacksonJsonProvider jsonProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new JacksonJsonProvider(objectMapper);
    }

    /**
     * Configures the custom prompt provider for the Shell.
     *
     * @return a PromptProvider with custom settings
     */
    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("Banking-Mico-SOA-Client:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }
}
