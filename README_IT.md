# Banking Micro-SOA System

Il “Sistema micro-SOA bancario” è un moderno sistema bancario che implementa un'architettura orientata ai servizi (SOA)
utilizzando microservizi con tecnologie REST e SOAP.

Questo progetto dimostra come costruire un'applicazione scalabile e modulare in grado di gestire le comuni operazioni
bancarie,
sfruttando le migliori pratiche di ingegneria del software e gli strumenti più avanzati.

Utilizza Apache CXF, Spring Boot, Docker e Maven.

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

![Use Case Diagram_it](https://www.plantuml.com/plantuml/png/ZLRTRjiW6BttKw3ikbMCxSIjgaYPf-rYcnejpGDGmXmK6oB0cjPfxpxyGmesUBc9p7VE-Vv1kI1yHYK1Y_rlgKWD3f88kG18zb_WJuImuW8iYaOgNXFHORWnve-8dGVZwzaOjyUQiix6Bz7VA184AyJAYlHyoahmdMB1mVvv2_h02_3qj6zUPNVOR7HS01f9C99Qz8sNWxz34OCFNq_eJ6ty2v6ASaR05cFUC3LGeOTQ28BUR2vm2ZiKoJpxlRH3aSvJhTOSYipcVY9AeRAYetnNtfeApXJNlq8GfiX0NNjAsrw8K8r0VaNkKCkIwhW8VERiI06g_9e7M_E3rhmlS5uKBnqLAQbKOcZDYq1CTjyKo2Ra87OTNBXGDogXjuDw49ZNoBGvJdnxmTbFMZOcqR746VKEbQWcRNpCsOIQtLQ-JxhwJ-4-pHfuQzw9zajHGyld5cOWSqDsvbwSQ2CzBBZqP3iA2sDlSiywy51IUqcGe6Q247glDoEcXKESjWjQni4EYJUAggi-kLNumfKbIyD3CzUvRRwxTM2o9p0pwUjNhts0xkyt_MsjA2EFlqvKaQWzz_Ps4nr06mOaTn3RMG8UzLj96QwQ8zbiB2GD8baGMOqGAxjy8ZB8Gn29bs4e3aDmd8NJYsbTxrWsWwrciBKlOcxGQO5N32vdiDZZ5PfMGy_ahbWwdGDC0ZEoe4ddR6pjyPc6VQOpFjEGJmkRLjzTY65h9pA3v44aJiV-khyaTtST29mZT4vhbvgunznLmYZ2ZJbaUcR_0000)

[Uses_cases_it.svg](/docs/Uses_cases_it.svg)

In questo scenario possiamo vedere tutti gli utenti del sistema che possono eseguire le azioni descritte in precedenza:

1. **Login:** Effettuare l'autenticazione e rilasciare il JWT.
2. **Gestione Account:** Creazione di un account della corretta tipologia utente, visualizzazione dettagli account.
3. **Gestione Conto Corrente:** Creazione conto corrente, aggiunta e rimozione fondi su un conto, gestione delle
   transazioni bancarie e generazione del report del conto corrente.
4. **Gestione Bancomat:** Configurazione di un bancomat, registrazione delle transazioni effettuate con il bancomat,
   generazione report del bancomat.
5. **Gestione Prestiti:** Apertura ed estinzione di un prestito, generazione del report sui prestiti.
6. **Registrazione Account con Conto Corrente e Bancomat:** Include la creazione di un account cliente, l'apertura di un
   conto corrente e la configurazione di un bancomat.
7. **Gestione Operazioni Bancarie:**
    1. **Generazione di un Report Parziale** che include:
        1. Dettagli Account
        2. Report Transazioni sul Conto Corrente
        3. Report Transazioni con Bancomat
    2. **Generazione di un Report Totale** che include:
        1. Dettagli Account
        2. Report Transazioni sul Conto Corrente
        3. Report Transazioni con Bancomat
        4. Report Prestiti
