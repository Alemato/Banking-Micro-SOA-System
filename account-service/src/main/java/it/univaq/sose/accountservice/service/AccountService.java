package it.univaq.sose.accountservice.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.univaq.sose.accountservice.domain.dto.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST interface for account management.
 */
@Path("/api/account")
public interface AccountService {

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param credentials the user credentials
     * @return the response containing the JWT token or an error message
     */
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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response login(@RequestBody(description = "Login",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserCredentials.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = UserCredentials.class)),
            }
    ) UserCredentials credentials);

    /**
     * Checks the validity of a JWT token.
     *
     * @param token the token to check
     * @return true if the token is valid, false otherwise
     */
    @Operation(operationId = "checkTokenResponse", description = "Check JWT", responses = {
            @ApiResponse(responseCode = "200", description = "Check successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = Boolean.class))})
    })
    @POST
    @Path("/check-token")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Boolean checkTokenResponse(@RequestBody(description = "Token",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TokenResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = TokenResponse.class)),
            }
    ) TokenResponse token);

    /**
     * Opens a new banker account.
     *
     * @param request the account creation request
     * @return the response indicating the result of the operation
     */
    @Operation(operationId = "openAccountBanker", description = "openAccountBanker", responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Save Banker Bank Account",
                    headers = @Header(name = "Location", description = "URL of the created resource", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @POST
    @Path("/banker-account")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response openAccountBanker(@RequestBody(description = "Account to be saved",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = OpenBankAccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = OpenBankAccountRequest.class)),
            }
    ) OpenBankAccountRequest request);

    /**
     * Opens a new admin account.
     *
     * @param request the account creation request
     * @return the response indicating the result of the operation
     */
    @Operation(operationId = "openAccountAdmin", description = "openAccountAdmin", responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Save Admin Bank Account",
                    headers = @Header(name = "Location", description = "URL of the created resource", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @POST
    @Path("/admin-account")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response openAccountAdmin(@RequestBody(description = "Account to be saved",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = OpenBankAccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = OpenBankAccountRequest.class)),
            }
    ) OpenBankAccountRequest request);

    /**
     * Opens a new customer account.
     *
     * @param request the account creation request
     * @return the response indicating the result of the operation
     */
    @Operation(operationId = "openAccountCustomer", description = "openAccountCustomer", responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Save Customer Bank Account",
                    headers = @Header(name = "Location", description = "URL of the created resource", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @POST
    @Path("/customer-account")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response openAccountCustomer(@RequestBody(description = "Account to be saved",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = OpenBankAccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = OpenBankAccountRequest.class)),
            }
    ) OpenBankAccountRequest request);

    /**
     * Adds a bank account ID to an existing account.
     *
     * @param id      the ID of the account to update
     * @param request the request containing the bank account ID to add
     * @return the response indicating the result of the operation
     */
    @Operation(operationId = "addBankAccount", description = "addBankAccount", responses = {
            @ApiResponse(description = "Save Bank Account on Account", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response addBankAccount(@PathParam(value = "id") long id, @RequestBody(description = "Bank Account to add",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AddIdBankAccountRequest.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML,
                            schema = @Schema(implementation = AddIdBankAccountRequest.class)),
            }
    ) AddIdBankAccountRequest request);

    /**
     * Retrieves an account by its ID.
     *
     * @param id the ID of the account to retrieve
     * @return the response containing the account details or an error message
     */
    @Operation(operationId = "getAccount", description = "getAccount", responses = {
            @ApiResponse(description = "Get Account by ID", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account with this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GET
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getAccount(@PathParam(value = "id") long id);
}
