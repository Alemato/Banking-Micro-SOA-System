package it.univaq.sose.financialreportserviceprosumer.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.univaq.sose.financialreportserviceprosumer.domain.ErrorResponse;
import it.univaq.sose.financialreportserviceprosumer.domain.FinancialReportResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;

@Path("/api/bank")
public interface FinancialReportService {
    @Operation(
            operationId = "getLoanByIdLoan",
            description = "This endpoint allows users to retrieve loans by ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loans Retrieved Successfully",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FinancialReportResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = FinancialReportResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Loan Not Found",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    )
            })
    @GET
    @Path("/financial-report/{idAccount}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void getFinancialReportByIdAccount(@PathParam("idAccount") long idAccount, @Suspended AsyncResponse asyncResponse);
}
