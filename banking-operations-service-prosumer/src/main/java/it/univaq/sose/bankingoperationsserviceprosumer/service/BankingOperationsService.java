package it.univaq.sose.bankingoperationsserviceprosumer.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.univaq.sose.bankingoperationsserviceprosumer.domain.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;

@Path("/api/bank")
public interface BankingOperationsService {
    @Operation(
            operationId = "OpenAccount",
            description = "This endpoint allows users to create their personal profile and open a new bank account in a single operation.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Open Account Successful",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OpenAccountResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = OpenAccountResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
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
    @POST
    @Path("/open-account")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void openAccount(@RequestBody(description = "OpenAccount",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OpenAccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = OpenAccountRequest.class)),
            }
    ) OpenAccountRequest openAccountRequest, @Suspended AsyncResponse asyncResponse);

    @Operation(
            operationId = "GetReportBankAccountByIdAccount",
            description = "This endpoint allows users to retrieve the report of a bank account using the account ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Report Retrieved Successfully",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ReportBankAccountResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ReportBankAccountResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
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
    @Path("/report-bank-account-by-account/{idAccount}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void getReportBankAccountFromIdAccount(@PathParam("idAccount") long idAccount, @Suspended AsyncResponse asyncResponse);

    @Operation(
            operationId = "requestAtmCard",
            description = "requestAtmCard",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Requesting an ATM card",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreateBancomatResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = CreateBancomatResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Account whit this Id not found",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    ),
            })
    @POST
    @Path("/atm-card/{accountId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void requestAtmCard(@Suspended AsyncResponse asyncResponse, @PathParam("accountId") long accountId);

    @Operation(
            operationId = "getAtmCard",
            description = "getAtmCard details",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "ATM card details",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreateBancomatResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = CreateBancomatResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Account whit this Id not found",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
                            }
                    )
            })
    @GET
    @Path("/atm-card/{accountId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void getAtmCard(@Suspended AsyncResponse asyncResponse, @PathParam("accountId") long accountId);
}
