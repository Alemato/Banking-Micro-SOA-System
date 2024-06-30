package it.univaq.sose.accountservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.jaxrs.swagger.ui.SwaggerUiConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApacheCXFConfig {

    @Value("${cxf.path}")
    private String cxfPath;

    @Bean
    public JacksonJsonProvider jsonProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new JacksonJsonProvider(objectMapper);
    }

    @Bean
    public OpenApiFeature createOpenApiFeature() {
        final OpenApiFeature openApiFeature = new OpenApiFeature();
        openApiFeature.setPrettyPrint(true);
        openApiFeature.setTitle("Spring Boot CXF REST Application");
        openApiFeature.setContactName("The Apache CXF team");
        openApiFeature.setDescription("This sample project demonstrates how to use CXF JAX-RS services"
                + " with Spring Boot. This demo has two JAX-RS class resources being"
                + " deployed in a single JAX-RS endpoint.");
        openApiFeature.setVersion("1.0.0");
        openApiFeature.setSwaggerUiConfig(
                new SwaggerUiConfig()
                        .url(cxfPath + "/openapi.json").queryConfigEnabled(false));
        return openApiFeature;
    }
}
