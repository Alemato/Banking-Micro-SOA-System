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

@Path("/api/account")
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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response login(@RequestBody(description = "Login",
            required = true,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserCredentials.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = UserCredentials.class)),
            }
    ) UserCredentials credentials);


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


    @Operation(operationId = "getAccount", description = "getAccount", responses = {
            @ApiResponse(description = "Get Account by ID", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = AccountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Account whit this Id not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GET
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getAccount(@PathParam(value = "id") long id);
}
