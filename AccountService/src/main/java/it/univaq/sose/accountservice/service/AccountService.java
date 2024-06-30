package it.univaq.sose.accountservice.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.univaq.sose.accountservice.domain.dto.*;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/account")
public interface AccountService {

    @Operation(operationId = "login", description = "Authenticate user and return JWT", responses = {
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TokenResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = TokenResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @POST
    @Path("/login")
    Response login(@RequestBody(description = "Login",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserCredentials.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = UserCredentials.class)),
            }
    ) UserCredentials credentials);


    @Operation(operationId = "saveAccountAndBankAccount", description = "saveAccountAndBankAccount", responses = {
            @ApiResponse(description = "Save Customer Bank Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountDto.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountDto.class))})
    })
    @POST
    @Path("/account/bank-account")
    AccountDto saveAccountAndBankAccount(@RequestBody(description = "Account to be saved",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = OpenBankAccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = OpenBankAccountRequest.class)),
            }
    ) OpenBankAccountRequest request);
}
