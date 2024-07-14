package it.univaq.sose.transactionserviceprosumer.service;


import it.univaq.sose.transactionserviceprosumer.domain.dto.ErrorResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class ServiceUnavailableExceptionMapper implements ExceptionMapper<ServiceUnavailableException> {
    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(ServiceUnavailableException exception) {
        MediaType responseType = determineResponseType();
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(new ErrorResponse(exception.getMessage()))
                .type(responseType)
                .build();
    }

    private MediaType determineResponseType() {
        List<MediaType> acceptableMediaTypes = headers.getAcceptableMediaTypes();
        if (acceptableMediaTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {
            return MediaType.APPLICATION_JSON_TYPE;
        } else if (acceptableMediaTypes.contains(MediaType.APPLICATION_XML_TYPE)) {
            return MediaType.APPLICATION_XML_TYPE;
        } else {
            return MediaType.TEXT_PLAIN_TYPE; // Default response type
        }
    }
}
