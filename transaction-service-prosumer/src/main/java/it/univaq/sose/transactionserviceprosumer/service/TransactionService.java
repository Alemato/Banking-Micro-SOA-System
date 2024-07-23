package it.univaq.sose.transactionserviceprosumer.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.univaq.sose.transactionserviceprosumer.domain.dto.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;

/**
 * Interface defining the transaction service for deposit, withdraw, transfer, and ATM payment operations.
 */
@Path("/api/transaction")
public interface TransactionService {

    /**
     * Deposit money into a bank account.
     *
     * @param asyncResponse        the async response
     * @param balanceUpdateRequest the balance update request
     */
    @Operation(operationId = "depositMoney", description = "depositMoney", responses = {
            @ApiResponse(responseCode = "201", description = "Deposit money in Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ExecuteTransactionResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ExecuteTransactionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service Unavailable",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    })
    @POST
    @Path("/deposit-money")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void depositMoney(@Suspended AsyncResponse asyncResponse,
                      @RequestBody(description = "Transaction for money deposit",
                              required = true,
                              content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                                      schema = @Schema(implementation = BalanceUpdateRequest.class)),
                                      @Content(mediaType = MediaType.APPLICATION_XML,
                                              schema = @Schema(implementation = BalanceUpdateRequest.class))}) BalanceUpdateRequest balanceUpdateRequest);

    /**
     * Withdraw money from a bank account.
     *
     * @param asyncResponse the async response
     * @param request       the balance update request
     */
    @Operation(operationId = "withdrawMoney", description = "depositMoney", responses = {
            @ApiResponse(responseCode = "201", description = "Withdraw money in Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ExecuteTransactionResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ExecuteTransactionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Insufficient funds", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service Unavailable",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    })
    @POST
    @Path("/withdraw-money")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void withdrawMoney(@Suspended AsyncResponse asyncResponse,
                       @RequestBody(description = "Transaction for money withdraw",
                               required = true,
                               content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                                       schema = @Schema(implementation = BalanceUpdateRequest.class)),
                                       @Content(mediaType = MediaType.APPLICATION_XML,
                                               schema = @Schema(implementation = BalanceUpdateRequest.class))}) BalanceUpdateRequest request);

    /**
     * Execute a transfer between bank accounts.
     *
     * @param asyncResponse the async response
     * @param request       the execute transfer request
     */
    @Operation(operationId = "executeTransfer", description = "executeTransfer", responses = {
            @ApiResponse(responseCode = "201", description = "Execute transfer from a bankAccount to another bankAccount", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ExecuteTransactionResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ExecuteTransactionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Insufficient funds", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service Unavailable",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    })
    @POST
    @Path("/execute-transfer")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void executeTransfer(@Suspended AsyncResponse asyncResponse,
                         @RequestBody(description = "Transfer",
                                 required = true,
                                 content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                                         schema = @Schema(implementation = ExecuteTransferRequest.class)),
                                         @Content(mediaType = MediaType.APPLICATION_XML,
                                                 schema = @Schema(implementation = ExecuteTransferRequest.class))}) ExecuteTransferRequest request);

    /**
     * Execute an ATM payment.
     *
     * @param asyncResponse              the async response
     * @param bancomatTransactionRequest the bancomat transaction request
     */
    @Operation(operationId = "executeAtmPayment", description = "executeAtmPayment", responses = {
            @ApiResponse(responseCode = "201", description = "Execute an ATM payment", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BancomatTransactionResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = BancomatTransactionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service Unavailable",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    })
    @POST
    @Path("/execute-atm-payment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void executeAtmPayment(@Suspended AsyncResponse asyncResponse, @RequestBody(description = "ATM payment",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = BancomatTransactionRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = BancomatTransactionRequest.class))}) BancomatTransactionRequest bancomatTransactionRequest);
}
