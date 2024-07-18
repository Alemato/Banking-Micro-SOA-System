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