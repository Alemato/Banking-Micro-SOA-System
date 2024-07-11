package it.univaq.sose.loanserviceprosumer.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.univaq.sose.loanserviceprosumer.domain.dto.ErrorResponse;
import it.univaq.sose.loanserviceprosumer.domain.dto.OpenLoanRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/loan")
public interface LoanService {

    @Operation(
            operationId = "OpenLoan",
            description = "This endpoint allows users to open a new loan.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Open Loan Successful",
                            headers = @Header(name = "Location", description = "URL of the created resource", schema = @Schema(type = "string"))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    )
            })
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void openLoan(@RequestBody(description = "OpenLoanRequest",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OpenLoanRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = OpenLoanRequest.class)),
            }) OpenLoanRequest openLoanRequest);
}
