# Banking Micro-SOA System

The 'Banking Micro-SOA System' is a modern banking system that implements a service-oriented architecture (SOA) using
microservices with REST and SOAP technologies.

This project demonstrates how to build a scalable and modular application capable of handling common banking operations,
exploiting software engineering best practices and state-of-the-art tools.

It uses Apache CXF, Spring Boot, Docker and Maven.

## Examination Requirements

The project in question meets all the requirements described in the following file:

[FINAL_TEST_23-24.pdf](/docs/FINAL_TEST_23-24.pdf)

## System Goals

The main objectives of the **Banking Micro SOA System** are:

- **Efficient User Management**: Ensure a secure and simple process for managing accounts and user authentication.
- **Account Operations Management**: Enable account and ATM opening, as well as the generation of detailed account
  status reports.
- **Comprehensive Current Account Management**: Allow for the addition and removal of funds, as well as the recording of
  all transactions.
- **Advanced ATM Management**: Facilitate the creation of ATMs and the recording of transactions conducted through these
  devices.
- **Loan Management**: Allow for the opening and closing of loans with precise tracking of related transactions.
- **Transaction Execution**: Support various operations such as transfers, withdrawals, deposits, and ATM payments.
- **Report Generation**: Provide a comprehensive financial report of all operations, offering a clear and integrated
  view of financial activities.

## System Operation

**The system is structured modularly**, dividing the main functionalities into specific services, each responsible for a
particular aspect of banking management:

- User Management:

    - Account Management: Creation, update, and deletion of user accounts.
      Authentication: Implementation of robust authentication systems to ensure secure access.

- Current Account Management:

    - Account Operations: Addition and removal of funds, detailed recording of all transactions.

- ATM Management:

    - ATM Creation: Configuration and management of ATM devices.
    - ATM Transactions: Saving and tracking operations conducted through ATMs.

- Loan Management:

    - Loan Opening/Closure: Procedures for the entire loan lifecycle, from request to closure.

- Transaction Execution:

    - Transfers, Withdrawals, Deposits, and ATM Payments: Execution and recording of all financial operations.

- Account Operations:

    - Account/ATM Opening: Process of opening new accounts and ATM devices.
    - Account Report: Generation of detailed reports on account status.

- Report Management:

    - Comprehensive Financial Report: Creation of integrated reports providing a global view of financial activities.

## Reasons for SOA and Microservices Approach

The adoption of an SOA and microservices architecture for the Banking Micro SOA System is motivated by several key
advantages:

- **Maintainability**: Dividing the system into independent microservices makes it easier to manage, update, and fix
  bugs. Each service can be developed, deployed, and maintained separately, reducing the risk of negative impacts on the
  overall system.

- **Scalability**: The ability to instantiate multiple nodes of the system allows for easy scaling of resources
  according to demand. In a banking environment with a high volume of transactions, this flexibility is crucial for
  ensuring optimal performance and service continuity.

- **Reliability**: The microservices-based architecture isolates faults, preventing a problem in a single component from
  compromising the entire system. This approach significantly enhances the reliability and resilience of the banking
  system.

- **Flexibility**: The use of SOA and microservices facilitates integration with other systems and the implementation of
  new functionalities without needing to rewrite the entire codebase, accelerating development and deployment times.

The **Banking Micro SOA System** thus represents a cutting-edge solution for banking management, capable of addressing
modern challenges with efficiency and robustness.

## Use Case Diagram

![Use Case Diagram_en](https://www.plantuml.com/plantuml/png/ZLRBRjim4BphAnRkEO4bIxOS10PS5UsXDHB4pWSmCYCJbaY3f1e4HV-zr8CsAP52RrxEpExESYduWl8pAZ3Czfz2uWfU1UPY1aXql-1FW7B9ECooMaXMOTvYUQsFlo9wxW_VplfmUwm8RS_O9VWR11pd4j6Yn1sVq09s9ESCzazRw0hFuF5nNx-9DjXiL5s0Mk0S2INwnOe-tsiMmfUV9tGc9hxDSrPJ2Jj4bPSAKzcJ8mTP6m6joZX6aZ1TO65bRfpucB79x0RXeoQkThzXYKZPJpjRsarohco0rzdAwSmMFLqmLRFk5QdjwzZSmlUQ7WL81WDfBrcRaXTSiTytqJjdbKSMpcrTpdXRxS0H5Mf5r5ZKPb2VEfpoPELumHV6hxh8CdZOZOSMBbpBevWPe--agBbvPM4yP3sZJpDPudRPCCqkDNqvJ3TXPGNs-YFgC9CwpBHK-zx3zOYGbKpK7Ej7CXyHh6wYSBZdZKrLf1kNS3mxrxjxHbmIL89EqDM07vXYQ-wHzG0FJFwF9dQzhnTS424x8dmGUHfk721wREvBPqbqs-gRrNmyxkyttLCh2SKF7oSYST34tNcpJHQW3deajH0Zrm8Ur8UEqBoiZtYpCP3OYoHUP3b0ZERIaKWZQoyIplrGw8UYSHUtIOnjtR1a0bjEO2j7mLI3OtjNB9fFOE4uQQJd73ckxOh53k72Sq46D1gRYJaSgZhUdF54pjYNqy2SvXEziYEPHb8l4iR3VEsVYRkx5WGhZAnePLCNTfZQoYW8ybe7IV0F)

[Uses_cases_EN.svg](/docs/Uses_cases_EN.svg)

In this scenario, we can see all the system users who can perform the actions described previously:

1. **Login:** Authenticate and issue the JWT.
2. **Account Management:** Create an account of the correct user type, view account details.
3. **Current Account Management:** Create a current account, add and remove funds from an account, manage bank
   transactions, and generate the current account report.
4. **ATM Management:** Configure an ATM, record transactions made with the ATM, generate the ATM report.
5. **Loan Management:** Open and close a loan, generate the loan report.
6. **Account Registration with Current Account and ATM:** Includes creating a customer account, opening a current
   account, and configuring an ATM.
7. **Banking Operations Management:**
    1. **Generate a Partial Report** that includes:
        1. Account Details
        2. Current Account Transaction Report
        3. ATM Transaction Report
    2. **Generate a Total Report** that includes:
        1. Account Details
        2. Current Account Transaction Report
        3. ATM Transaction Report
        4. Loan Report

## Component Diagram

![SympleBankingSystem-component_EN.svg](docs/SympleBankingSystem-component_EN.svg)

### Components

1. **Banking Mini SOA System**
    - Represents the main system that includes all subsystems and services.

2. **Account Service**
    - Type: REST Service Provider
    - Responsibilities: Manages account-related operations such as authentication, creation, and adding accounts. It
      handles data persistence using a database to store account information.
    - Operations:
        - `OpenAccount (Customer/Banker/Admin)`
        - `Login`
        - `AddBankAccount`
        - `GetAccount`
        - `CheckTokenResponse`

3. **Bank Account Service Provider**
    - Type: SOAP Service Provider
    - Responsibilities: Manages current accounts, adds and removes money, and saves transactions. Keeps track of all
      bank account information and related transactions using a database.
    - Operations:
        - `CreateBankAccount`
        - `AddMoney`
        - `RemoveMoney`
        - `BancomatPay`
        - `ExecuteTransfer`
        - `CheckBankAccountTransfer`
        - `GetBankAccountTransaction`
        - `GetBankAccountDetails`

4. **Bancomat Service**
    - Type: SOAP Service Provider
    - Responsibilities: Manages ATM operations and related transactions. Handles data persistence using a database to
      store ATM information and transactions.
    - Operations:
        - `CreateBancomat`
        - `GetBancomatDetails`
        - `GetBancomatDetailsByNumber`
        - `ExecuteTransaction`
        - `GetBancomatTransaction`

5. **Banking Operations Service Prosumer**
    - Type: REST Service Prosumer
    - Responsibilities: Manages banking operations for opening accounts.
    - Operations:
        - `OpenAccount`
        - `GetReportBankAccountFromIdAccount`
        - `RequestAtmCard`
        - `GetAtmCard`

6. **Loan Service Provider**
    - Type: REST Service Provider
    - Responsibilities: Manages loan-related operations such as creation and closure, and keeps track of the user's loan
      history.
    - Operations:
        - `OpenLoan`
        - `CloseLoanByIdLoan`
        - `GetLoanByIdLoan`
        - `GetAllLoanByIdAccount`
        - `GetAllLoanByIdBankAccount`

7. **Transaction Service Prosumer**
    - Type: REST Service Prosumer
    - Responsibilities: Manages financial transactions such as deposits, withdrawals, transfers, and ATM payments.
    - Operations:
        - `DepositMoney`
        - `WithdrawMoney`
        - `ExecuteTransfer`
        - `ExecuteAtmPayment`

8. **Financial Report Service Provider**
    - Type: REST Service Provider
    - Responsibilities: Generates and provides comprehensive financial reports of the entire banking position of the
      account.
    - Operations:
        - `GetFinancialReportByIdAccount`

9. **Gateway Service**
    - Type: Gateway
    - Responsibilities: Provides a unified entry point for the banking system services.
    - Operations:
        - `HandleRequests`

10. **Discovery Service**
    - Type: Discovery
    - Responsibilities: Manages the discovery and integration of services within the system.

11. **Banking Service Client**
    - Type: Client Spring Shell
    - Responsibilities: Interfaces with the various banking system services through the gateway to execute
      user-requested operations.

### Additional Notes

- The **Banking Service Client** communicates with the **Gateway Service** for all operations.
- The **Gateway Service** routes requests to the respective providers/prosumers.
- Services follow SOA/Microservices paradigms with REST or SOAP interfaces, ensuring interoperability and scalability.
- Load balancing is defined according to a logic of *“Non-repetitive random iteration”*.
- Services are designed to be modular and can be updated or replaced independently without affecting the entire system.

## Sequence Diagrams

### Open Bank Account

![open_bank_account_seq.svg](docs/open_bank_account_seq.svg)

In this operation, the customer user opens their current account by registering with the banking system. The first
request is made to the Banking Operation Service Prosumer, which in turn first requests the creation of the account on
the Account Service Provider, then creates the account on the BankAccount Service Provider. After that, the account is
updated on the Account Service Provider by inserting the ID of the newly created account, and finally, the ATM card is
created on the Bancomat Service Provider. At the end of the operations, the creation response is returned to the client.

### Login

![Login_seq.svg](docs/Login_seq.svg)

During this operation, the registered user authenticates with the system. The user sends their credentials to the
system, which forwards them to the Account Service. The Account Service verifies the credentials and generates a JWT
token. At the end of the process, a response containing the generated token is sent back.

### Financial Report

![GetFinancialReportByIdAccount_seq.svg](docs/GetFinancialReportByIdAccount_seq.svg)

During this operation, the user requests the complete financial report from the system. The user sends the request to
the system, which forwards it to the Financial Report Service Prosumer. This service will send four parallel requests:

1. `GetBancomatDetails` to the Bancomat Service (SOAP)
2. `GetBancomatTransactions` to the Bancomat Service (SOAP)
3. `GetReportBankAccountFromIdAccount` to the Banking Operations Service Prosumer (REST)
4. `GetAllLoanByIdAccount` to the Loan Service Prosumer (REST)

(The `GetReportBankAccountFromIdAccount` operation will be described later.)

At the end of the process, the four responses are combined to generate the complete financial report.

### Bank Account Report

![GetReportBankAccountFromIdAccount_seq.svg](docs/GetReportBankAccountFromIdAccount_seq.svg)

During this operation, the user requests the current account report from the system. The user sends the request to the
system, which forwards it to the Banking Operation Service Prosumer. This service makes two calls: first to the Account
Service to get the account information, and then to the Bank Account Service to get the bank account information.

At the end of the process, the information is combined to generate the bank account report.

### Withdraw Money

![withdraw_money_seq.svg](docs/withdraw_money_seq.svg)

The operation of withdrawing money from the current account is executed by the customer user. Through the Transaction
Service Prosumer, a request is forwarded to the Bank Account Service to verify if there is enough money in the current
account, and the amount is subtracted from the balance. Immediately after, a withdrawal transaction is created and
returned to the client.

### Open Loan

![open_loan_seq.svg](docs/open_loan_seq.svg)

The opening of a loan by the customer user involves the Loan Service Prosumer and the BankAccount Service Provider.
Initially, a loan is created, and then the corresponding amount of money is added to the user's current account on the
BankAccount Service Provider. Lastly, before returning the response, the loan opening transaction is also saved on the
user's account.

### Close Loan

![close_loan_seq.svg](docs/close_loan_seq.svg)

The operation of closing a loan, executed by a customer user, involves the extinction of the loan by subtracting the
corresponding amount of money from the user's current account on the BankAccount Service Provider. The account balance
is checked and the amount is subtracted. Finally, the status of the loan is updated before returning the response.

---

# Implementation

For each module of the project, Spring Boot 3.3.1 and Apache CXF 4.0.4 were chosen.

In compliance with exam requirements, no technologies other than those explained and adopted in class were used.

The project is divided into the following Maven modules:

1. **account-service**: Provider. Responsible for authentication and user management operations. This service was
   implemented in REST using Apache CXF (JAX-RS) and Spring Boot, registering via Netflix Eureka.

2. **bank-account-services**: Provider. Responsible for operations related to current accounts. This service was
   implemented in SOAP using Apache CXF (JAX-WS) and Spring Boot, registering via Netflix Eureka.

3. **bancomat-service**: Provider. Responsible for operations related to ATMs. This service was implemented in SOAP
   using Apache CXF (JAX-WS) and Spring Boot, registering via Netflix Eureka.

4. **banking-operations-service-prosumer**: Prosumer. Responsible for banking operations such as opening accounts. This
   service was implemented in REST using Apache CXF (JAX-RS) and Spring Boot, registering via Netflix Eureka. All
   operations are provided asynchronously. REST and SOAP clients that implement load balancing to the producer service
   were developed.

5. **loan-service-prosumer**: Prosumer. Responsible for loan operations. This service was implemented in REST using
   Apache CXF (JAX-RS) and Spring Boot, registering via Netflix Eureka. All operations are provided asynchronously. A
   REST client that implements load balancing to the producer service was developed.

6. **transaction-service-prosumer**: Prosumer. Responsible for managing financial transactions such as deposits,
   withdrawals, transfers, and ATM payments. This service was implemented in REST using Apache CXF (JAX-RS) and Spring
   Boot, registering via Netflix Eureka. All operations are provided asynchronously. REST and SOAP clients that
   implement load balancing to the producer service were developed.

7. **financial-report-service-prosumer**: Prosumer. Responsible for generating and providing comprehensive financial
   reports of the account's banking position. This service was implemented in REST using Apache CXF (JAX-RS) and Spring
   Boot, registering via Netflix Eureka. All operations are provided asynchronously. REST and SOAP clients that
   implement load balancing to the producer service were developed.

8. **gateway-service**: Gateway. Responsible for providing a single unified entry point for our system's services. This
   service was implemented with Spring Cloud Reactive Gateway and uses Netflix Eureka.

9. **discovery-service**: Discovery. Responsible for managing service discovery and integration within the system. This
   service was implemented with Spring Cloud Discovery Eureka.

10. **banking-service-client**: Client. This is the example client that interfaces with the Gateway to perform banking
    operations. This client was developed with Spring Shell and Apache CXF (REST client).

## Integrating Spring Boot and Apache CXF

To integrate Apache CXF into a Spring Boot application, you need to add one of the following dependencies in
the `pom.xml`:

```xml

<dependencies>
    <!-- ...  -->
    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-spring-boot-starter-jaxws</artifactId>
        <version>4.0.4</version>
    </dependency>

    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-spring-boot-starter-jaxrs</artifactId>
        <version>4.0.4</version>
    </dependency>
    <!-- ...  -->
</dependencies>
```

- **cxf-spring-boot-starter-jaxws**: Configures and runs Apache CXF in a Spring application, integrating the necessary
  dependencies for running Apache CXF in SOAP mode (JAX-WS).

- **cxf-spring-boot-starter-jaxrs**: Configures and runs Apache CXF in a Spring application, integrating the necessary
  dependencies for running Apache CXF in REST mode (JAX-RS).

**Note**: If you want to implement a Spring Boot application that uses Apache CXF in both SOAP and REST modes, you only
need to include one of the dependencies, along with the client dependency for Apache CXF in the other mode. For example,
if you include only the `cxf-spring-boot-starter-jaxws` dependency (for SOAP), you just need to add the missing library
dependency `cxf-rt-rs-client` for the REST client in the `pom.xml`.

### Configuring Apache CXF for Proper Execution

To create a SOAP application, you need to manually configure Apache CXF. Here is the necessary Java code:

```java

@Configuration
public class ApacheCXFConfig {

    private final Bus bus;
    private final BankAccountService bankAccountService;
    private final MetricsProvider metricsProvider;

    public ApacheCXFConfig(Bus bus, BankAccountService bankAccountService, MetricsProvider metricsProvider) {
        this.bus = bus;
        this.bankAccountService = bankAccountService;
        this.metricsProvider = metricsProvider;
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, bankAccountService, null, null, new MetricsFeature[]{
                new MetricsFeature(metricsProvider)
        });
        endpoint.publish("/BankAccountService");
        return endpoint;
    }

}
```

As seen in the code, you need to manually create a Java Bean that provides the proper initialization of an **Apache CXF
Endpoint**. Through dependency injection, Spring will inject: the `Bus` from Apache CXF, `BankAccountService` which is
the interface defining the SOAP services, and `MetricsProvider` which is the metrics provider (we will discuss this
further later).

To create a REST application, you can use Spring's component-scan to find all the classes needed by Apache CXF and
Spring to properly configure the service. The configuration of Apache CXF can be done using the following YAML commands:

```yaml
cxf:
  path: /services
  servlet.init:
    service-list-path: /info
  jaxrs:
    component-scan: true
    classes-scan-packages: it.univaq.sose.accountservice.configuration, it.univaq.sose.accountservice.service
```

As seen, under the `cxf.jaxrs` key, there are settings for the component-scan. You need to add to this key every package
that contains Apache CXF code (endpoints and various configurations). This way, Apache CXF will check the package for
configurations or endpoints.

In the YAML file, there are also the `cxf.path` and `cxf.servlet.init.service-list-path` keys, if set:

- **cxf.path**: Identifies the path of the servlet used by Apache CXF within the application.
- **cxf.servlet.init.service-list-path**: Identifies the path where Apache CXF will publish the list of managed
  services.

## Integration of Apache CXF Service Description OpenApi in Spring Boot

To integrate Apache CXF's Service Description OpenApi into Spring Boot, add the following dependencies to
your `pom.xml`:

```xml

<dependencies>
    <!-- ... -->
    <dependency>
        <groupId>org.webjars</groupId>
        <artifactId>swagger-ui</artifactId>
        <version>5.17.14</version>
    </dependency>
    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-rt-rs-service-description</artifactId>
        <version>4.0.4</version>
    </dependency>
    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-rt-rs-service-description-openapi-v3</artifactId>
        <version>4.0.4</version>
    </dependency>
    <!-- ... -->
</dependencies>
```

After adding the dependencies, create a Java Bean to provide a properly configured instance of `OpenApiFeature`:

```java

@Configuration
public class ApacheCXFConfig {
    @Value("${cxf.path}")
    private String cxfPath;

    @Bean
    public OpenApiFeature createOpenApiFeature() {
        final OpenApiFeature openApiFeature = new OpenApiFeature();
        openApiFeature.setPrettyPrint(true);
        openApiFeature.setTitle("Account Service for Banking Micro-SOA System");
        openApiFeature.setContactName("The Banking Micro-SOA System team");
        openApiFeature.setDescription("This is Account Service for Banking Micro-SOA System. Uses Apache CXF and Spring Boot on JAX-RS.");
        openApiFeature.setVersion("0.0.1-SNAPSHOT");
        openApiFeature.setSwaggerUiConfig(
                new SwaggerUiConfig()
                        .url(cxfPath + "/openapi.json").queryConfigEnabled(false));
        return openApiFeature;
    }

    // ...
}
```

Now you can use OpenApi annotations within your REST service interfaces.

## Integration of Apache CXF Logging in Spring Boot

To integrate Apache CXF Logging functionality in Spring Boot, add the following dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-rt-features-logging</artifactId>
    <version>4.0.4</version>
</dependency>
```

After adding the dependency, create a Java Bean to provide a properly configured instance of `LoggingFeature`:

```java

@Configuration
public class ApacheCXFConfig {
    // ...

    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        return loggingFeature;
    }
}
```

## Modifications to Jackson JSON Provider for Apache CXF in Spring Boot

To properly handle dates during serialization and deserialization with the Jackson provider, add the following
dependencies:

```xml

<dependencies>
    <!-- ...  -->
    <dependency>
        <groupId>com.fasterxml.jackson.jakarta.rs</groupId>
        <artifactId>jackson-jakarta-rs-json-provider</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-annotations</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
    </dependency>
    <!-- ...  -->
</dependencies>
```

To properly configure the Jackson Provider, including support for the new Java 8 date and time classes and setting date
serialization to a readable format instead of timestamps, create a Java Bean configured as follows:

```java

@Configuration
public class ApacheCXFConfig {
    // ...
    @Bean
    public JacksonJsonProvider jsonProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new JacksonJsonProvider(objectMapper);
    }
}
```

This will enable support for the ISO-8601 standard.

An adapter is also provided to manually handle dates:

```java
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime unmarshal(String v) {
        return LocalDateTime.parse(v, DATE_FORMATTER);
    }

    @Override
    public String marshal(LocalDateTime v) {
        return v != null ? DATE_FORMATTER.format(v) : null;
    }
}
```

Example usage of the adapter:

```java

@Data
@XmlRootElement(name = "AccountResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 4592896323731902686L;

    // ...

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime updateDate;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime createDate;

    // ...
}
```

This code ensures that dates are properly handled during serialization and deserialization, adhering to the ISO-8601
standard.

## Integration of Apache CXF Metrics with Spring Boot Actuator

In this project, we use Spring Boot Actuator to generate application metrics.

To manage and create Apache CXF metrics, you need to add the `cxf-rt-features-metrics` dependency.

Insert the following dependencies into the `pom.xml` file:

```xml

<dependencies>
    <!-- ...  -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-rt-features-metrics</artifactId>
        <version>4.0.4</version>
    </dependency>
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-core</artifactId>
    </dependency>
    <!-- ...  -->
</dependencies>
```

#### Metrics Configuration for a SOAP Application

To enable metrics in a SOAP application, insert an instance of `MetricsFeature` in the constructor of the `Endpoint`:

```java

@Configuration
public class ApacheCXFConfig {

    private final Bus bus;
    private final BancomatService bancomatService;
    private final MetricsProvider metricsProvider;

    public ApacheCXFConfig(Bus bus, BancomatService bancomatService, MetricsProvider metricsProvider) {
        this.bus = bus;
        this.bancomatService = bancomatService;
        this.metricsProvider = metricsProvider;
    }

    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        return loggingFeature;
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, bancomatService, null, null, new MetricsFeature[]{
                new MetricsFeature(metricsProvider)
        });
        endpoint.publish("/BancomatService");
        return endpoint;
    }
}
```

#### Metrics Configuration for a REST Application

For a REST application, you need to add the metrics provider package to the component-scan package list:

```yaml
cxf:
  path: /services
  servlet.init:
    service-list-path: /info
  jaxrs:
    component-scan: true
    classes-scan-packages: org.apache.cxf.metrics, it.univaq.sose.accountservice.configuration, it.univaq.sose.accountservice.service
```

#### Spring Boot Actuator Configuration

Configure Spring Boot Actuator with the following parameters:

```yaml
management:
  endpoints.web.exposure.include: health,info
  info.env.enabled: true

info.application:
  name: account-service
  description: Account Service for Banking Micro-SOA System
  version: 0.0.1-SNAPSHOT
```

These configurations allow the integration of Apache CXF metrics with Spring Boot Actuator, providing full visibility
into the application's performance.

## Spring Cloud Discovery Eureka

In this project, we use **Spring Cloud Discovery Eureka** as a discovery service for the entire system.

#### Creating the Eureka Server

To integrate Spring Cloud Discovery Eureka, you need to create a server as a separate Spring Boot application.

##### Configuration of the Server `pom.xml`

Below is the `pom.xml` file for the Eureka server:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.1</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>it.univaq.sose</groupId>
    <artifactId>discovery-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>discovery-service</name>
    <description>Discovery Service</description>

    <properties>
        <java.version>17</java.version>
        <spring-cloud.version>2023.0.2</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

#### Eureka Server Configuration

The standard configuration of Eureka involves multiple redundant nodes to ensure the reliability of the discovery
service. However, to adapt Eureka to the specific needs of our system, we will modify some settings:

```yaml
eureka:
  instance:
    hostname: ${EUREKA_HOSTNAME:localhost}
  server:
    enable-self-preservation: false
    expected-client-renewal-interval-seconds: 15
    eviction-interval-timer-in-ms: 2000
    renewal-threshold-update-interval-ms: 2000
    renewal-percent-threshold: 0.5
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

#### Integrating the Eureka Client in Modules

To use the created discovery service, we need to add the following dependency in the `pom.xml` of our modules:

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>2023.0.2</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
</dependencyManagement>
```

#### Eureka Client Configuration

To increase the responsiveness of Eureka, we configure the client as follows:

```yaml
eureka:
  client:
    registerWithEureka: true
    serviceUrl:
      defaultZone: http://${EUREKA_HOST:host.docker.internal}:${EUREKA_SERVER_PORT:8761}/eureka/
  instance:
    lease-expiration-duration-in-seconds: 11
    lease-renewal-interval-in-seconds: 5
    prefer-ip-address: true
    statusPageUrlPath: /services/info
    healthCheckUrlPath: /actuator/health
    metadataMap:
      servletPath: ${cxf.path}
```

##### Client Configuration Details

1. **registerWithEureka: true**
    - Indicates that the client should register with the Eureka server, allowing the server to route requests to it.
2. **serviceUrl.defaultZone**
    - Specifies the URL of the Eureka server with which the client should register. Uses environment variables to
      determine the Eureka server host and port.
3. **lease-expiration-duration-in-seconds: 11**
    - Specifies the duration of the lease. After 11 seconds without renewal, the lease expires, and the Eureka server
      can remove the instance.
4. **lease-renewal-interval-in-seconds: 5**
    - Indicates the frequency with which the client sends a heartbeat signal to the Eureka server to renew the lease,
      useful for development or debugging.
5. **prefer-ip-address: true**
    - Configures the instance to prefer the IP address during registration and communication with the Eureka server,
      resolving container name resolution issues.
6. **statusPageUrlPath: /services/info**
    - Specifies the URL path for the instance's status page, which contains the list of services implemented by Apache
      CXF.
7. **healthCheckUrlPath: /actuator/health**
    - Indicates the URL path for the instance's health check.
8. **metadataMap.servletPath: ${cxf.path}**
    - Defines a metadata map that includes the servletPath, whose value is the root of Apache CXF.