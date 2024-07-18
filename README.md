# Banking Micro-SOA System

The 'Banking Micro-SOA System' is a modern banking system that implements a service-oriented architecture (SOA) using
microservices with REST and SOAP technologies.

This project demonstrates how to build a scalable and modular application capable of handling common banking operations,
exploiting software engineering best practices and state-of-the-art tools.

It uses Apache CXF, Spring Boot, Docker and Maven.

## Requisiti di Esame

Il progetto in questione rispecchia tutti i requisiti richiesti e descritti dal seguente file:
[FINAL_TEST_23-24.pdf](/docs/FINAL_TEST_23-24.pdf)

## Obiettivi del Sistema

Gli obiettivi principali del sistema **Banking Micro SOA System** sono:

- **Gestione Efficiente degli Utenti**: Garantire un processo sicuro e semplice di gestione degli account e
  autenticazione
  degli utenti.
- **Gestione delle Operazioni sul Conto**: Abilitare l'apertura di conti e bancomat, oltre alla generazione di report
  dettagliati sullo stato dei conti.
- **Gestione Completa dei Conti Correnti**: Permettere operazioni di aggiunta e rimozione di denaro, nonché la
  registrazione
  di tutte le transazioni.
- **Gestione Avanzata dei Bancomat**: Consentire la creazione di bancomat e la registrazione delle transazioni
  effettuate
  tramite questi dispositivi.
- **Gestione dei Prestiti**: Consente l'apertura e l'estinzione dei prestiti con tracciamento preciso delle transazioni
  correlate.
- **Esecuzione delle Transazioni**: Supportare varie operazioni come bonifici, prelievi, depositi e pagamenti bancomat.
- **Generazione di Report**: Fornire un report finanziario complessivo di tutte le operazioni, offrendo una visione
  chiara e
  integrata delle attività finanziarie.

## Funzionamento del Sistema

**Il sistema è strutturato in modo modulare**, suddividendo le principali funzionalità in servizi specifici, ciascuno
responsabile di un particolare aspetto della gestione bancaria:

- Gestione dell'Utenza:

    - Account Management: Creazione, aggiornamento e cancellazione degli account utente.
      Autenticazione: Implementazione di sistemi di autenticazione robusti per garantire la sicurezza degli accessi.

- Gestione del Conto Corrente:

    - Operazioni sul Conto: Aggiunta e rimozione di fondi, registrazione dettagliata di tutte le transazioni.

- Gestione del Bancomat:

    - Creazione Bancomat: Configurazione e gestione dei dispositivi bancomat.
    - Transazioni Bancomat: Salvataggio e tracciamento delle operazioni effettuate tramite bancomat.

- Gestione dei Prestiti:

    - Apertura/Estinzione Prestiti: Procedure dell'intero ciclo di vita dei prestiti, dalla richiesta alla chiusura.

- Esecuzione delle Transazioni:

    - Bonifici, Prelievi, Depositi e Pagamenti Bancomat: Esecuzione e registrazione di tutte le operazioni finanziarie.

- Operazioni sul Conto:

    - Apertura Conto/Bancomat: Processo di apertura di nuovi conti e dispositivi bancomat.
    - Report Conto: Generazione di report dettagliati sullo stato dei conti.

- Gestione dei Report:

    - Report Finanziario Complessivo: Creazione di report integrati che forniscono una visione globale delle attività
      finanziarie.

## Motivazioni legate alll'Approccio SOA e Microservices

L'adozione di un'architettura SOA e microservizi per il Banking Micro SOA System è motivata da diversi vantaggi chiave:

- **Manutenibilità**: La divisione del sistema in microservizi indipendenti rende più semplice la gestione,
  l'aggiornamento e
  la correzione di errori. Ogni servizio può essere sviluppato, distribuito e mantenuto separatamente, riducendo il
  rischio di impatti negativi sul sistema complessivo.

- **Scalabilità**: La possibilità di istanziare più nodi del sistema permette di scalare facilmente le risorse in base
  alla
  domanda. In un ambiente bancario con un alto volume di transazioni, questa flessibilità è cruciale per garantire
  prestazioni ottimali e continuità del servizio.

- **Affidabilità**: L'architettura basata su microservizi isola i guasti, impedendo che un problema in un singolo
  componente
  comprometta l'intero sistema. Questo approccio aumenta significativamente l'affidabilità e la resilienza del sistema
  bancario.

- **Flessibilità**: L'uso di SOA e microservizi facilita l'integrazione con altri sistemi e l'implementazione di nuove
  funzionalità senza dover riscrivere l'intero codice base, accelerando i tempi di sviluppo e distribuzione.

Il sistema **Banking Micro SOA System** rappresenta quindi una soluzione all'avanguardia per la gestione bancaria,
capace di
rispondere alle sfide moderne con efficienza e robustezza.

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