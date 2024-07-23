package it.univaq.sose.bankingoperationsserviceprosumer.service;

import it.univaq.sose.bankingoperationsserviceprosumer.domain.ErrorResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

/**
 * Exception mapper to handle ServiceUnavailableException.
 * This class maps the exception to a proper HTTP response with a status code of 503 (Service Unavailable).
 */
@Provider
public class ServiceUnavailableExceptionMapper implements ExceptionMapper<ServiceUnavailableException> {
    @Context
    private HttpHeaders headers;

    /**
     * Converts a ServiceUnavailableException into a Response object.
     *
     * @param exception the exception to be mapped to a response.
     * @return a Response object with a 503 status code and an appropriate error message.
     */
    @Override
    public Response toResponse(ServiceUnavailableException exception) {
        MediaType responseType = determineResponseType();
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(new ErrorResponse(exception.getMessage()))
                .type(responseType)
                .build();
    }

    /**
     * Determines the response type (media type) based on the acceptable media types in the HTTP headers.
     *
     * @return the media type for the response.
     */
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
