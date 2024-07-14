package it.univaq.sose.loanserviceprosumer.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.univaq.sose.loanserviceprosumer.domain.dto.ErrorResponse;
import it.univaq.sose.loanserviceprosumer.domain.dto.LoanDto;
import it.univaq.sose.loanserviceprosumer.domain.dto.OpenLoanRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
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
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LoanDto.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = LoanDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
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
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void openLoan(@RequestBody(description = "OpenLoanRequest",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OpenLoanRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = OpenLoanRequest.class)),
            }) OpenLoanRequest openLoanRequest, @Suspended AsyncResponse asyncResponse);

    @Operation(
            operationId = "getLoanByIdLoan",
            description = "This endpoint allows users to retrieve loans by ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loans Retrieved Successfully",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = LoanDto.class))),
                                    @Content(mediaType = MediaType.APPLICATION_XML, array = @ArraySchema(schema = @Schema(implementation = LoanDto.class)))
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
    @Path("/{idLoan}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void getLoanByIdLoan(@PathParam("idLoan") long idLoan, @Suspended AsyncResponse asyncResponse);

    @Operation(
            operationId = "getAllLoanByIdBankAccount",
            description = "This endpoint allows users to retrieve all loans by bank account ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loans Retrieved Successfully",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = LoanDto.class))),
                                    @Content(mediaType = MediaType.APPLICATION_XML, array = @ArraySchema(schema = @Schema(implementation = LoanDto.class)))
                            }),
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
    @Path("/bank-account/{idBankAccount}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void getAllLoanByIdBankAccount(@PathParam("idBankAccount") long idBankAccount, @Suspended AsyncResponse asyncResponse);


    @Operation(
            operationId = "getAllLoanByIdAccount",
            description = "This endpoint allows users to retrieve all loans by account ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loans Retrieved Successfully",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = LoanDto.class))),
                                    @Content(mediaType = MediaType.APPLICATION_XML, array = @ArraySchema(schema = @Schema(implementation = LoanDto.class)))
                            }),
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
    @Path("/account/{idAccount}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void getAllLoanByIdAccount(@PathParam("idAccount") long idAccount, @Suspended AsyncResponse asyncResponse);

    @Operation(
            operationId = "closeLoanByIdLoan",
            description = "This endpoint allows users to close a loan by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan Closed Successfully"),
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
    @PUT
    @Path("/close-loan/{idLoan}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void closeLoanByIdLoan(@PathParam("idLoan") long idBankAccount, @Suspended AsyncResponse asyncResponse);
}
