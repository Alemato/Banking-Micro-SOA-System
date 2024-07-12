package it.univaq.sose.transactionserviceprosumer.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.univaq.sose.transactionserviceprosumer.domain.dto.BalanceUpdateRequest;
import it.univaq.sose.transactionserviceprosumer.domain.dto.ErrorResponse;
import it.univaq.sose.transactionserviceprosumer.domain.dto.ExecuteTransactionResponse;
import it.univaq.sose.transactionserviceprosumer.domain.dto.ExecuteTransferRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;


@Path("/api/transaction")
public interface TransactionService {

    @Operation(operationId = "depositMoney", description = "depositMoney", responses = {
            @ApiResponse(description = "Deposit money in Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ExecuteTransactionResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ExecuteTransactionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
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

    @Operation(operationId = "withdrawMoney", description = "depositMoney", responses = {
            @ApiResponse(description = "Withdraw money in Bank Account", content = {
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
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
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

    @Operation(operationId = "executeTransfer", description = "executeTransfer", responses = {
            @ApiResponse(description = "Execute transfer from a bankAccount to another bankAccount", content = {
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
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
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


    @Operation(operationId = "requestAtmCard", description = "requestAtmCard", responses = {
            @ApiResponse(description = "Requesting an ATM card", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ExecuteTransactionResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ExecuteTransactionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @POST
    @Path("/request-atm-card/{accountId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void requestAtmCard(@Suspended AsyncResponse asyncResponse, @PathParam(value = "accountId") Long bankAccountId);


}
