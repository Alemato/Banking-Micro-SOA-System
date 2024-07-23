package it.univaq.sose.gatewayservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.sose.gatewayservice.domain.ErrorResponse;
import it.univaq.sose.gatewayservice.security.AuthenticationException;
import it.univaq.sose.gatewayservice.security.JWTVerification;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.StringWriter;
import java.util.List;

/**
 * GatewayFilter for handling authorization based on JWT tokens.
 */
@Slf4j
@Service
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JWTVerification jwtVerification;

    public AuthorizationFilter(JWTVerification jwtVerification) {
        super(Config.class);
        this.jwtVerification = jwtVerification;
    }

    /**
     * Applies the filter to the Gateway.
     *
     * @param config the configuration object for the filter.
     * @return the GatewayFilter.
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Authorization header is missing or invalid", HttpStatus.BAD_REQUEST);
            }
            String token = authHeader.substring(7);
            long identifier;
            String role;

            try {
                jwtVerification.checkRoles(token, config.getRoles());
                identifier = jwtVerification.getIdentifier(token);
                role = jwtVerification.getRole(token);
                jwtVerification.verifyToken(token);
            } catch (AuthenticationException e) {
                return onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
            }

            if (config.isCheckPathWithIdentifier() && "CUSTOMER".equals(role) && !exchange.getRequest().getPath().toString().contains(Long.toString(identifier))) {
                log.error("Errore conf:{}; Customer:{} Ident:{}", config.isCheckPathWithIdentifier(), "CUSTOMER".equals(role), !exchange.getRequest().getPath().toString().contains(Long.toString(identifier)));
                return onError(exchange, "Forbidden: operation not allowed", HttpStatus.FORBIDDEN);
            }

            return chain.filter(exchange);

        };
    }

    /**
     * Configuration class for the AuthorizationFilter.
     */
    @Getter
    @Setter
    @Builder
    public static class Config {
        private List<String> roles;
        private boolean checkPathWithIdentifier;
    }

    /**
     * Handles errors by sending an appropriate response.
     *
     * @param exchange   the ServerWebExchange.
     * @param error      the error message.
     * @param httpStatus the HTTP status to set.
     * @return a Mono that completes the response handling.
     */
    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        ErrorResponse errorResponse = new ErrorResponse(error);
        MediaType mediaType = exchange.getRequest().getHeaders().getAccept().stream()
                .filter(mt -> mt.equals(MediaType.APPLICATION_JSON) || mt.equals(MediaType.APPLICATION_XML))
                .findFirst()
                .orElse(MediaType.APPLICATION_JSON);
        exchange.getResponse().getHeaders().setContentType(mediaType);
        try {
            byte[] bytes;
            if (mediaType.equals(MediaType.APPLICATION_JSON)) {
                bytes = objectMapper.writeValueAsBytes(errorResponse);
            } else {
                JAXBContext jaxbContext = JAXBContext.newInstance(ErrorResponse.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                StringWriter sw = new StringWriter();
                marshaller.marshal(errorResponse, sw);
                bytes = sw.toString().getBytes();
            }
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
