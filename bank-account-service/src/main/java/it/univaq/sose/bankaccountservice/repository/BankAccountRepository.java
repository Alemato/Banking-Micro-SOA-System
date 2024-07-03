package it.univaq.sose.bankaccountservice.repository;

import it.univaq.sose.bankaccountservice.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountId(Long accountId);

    Optional<BankAccount> findByIban(String iban);

    boolean existsByAccountId(Long accountId);
}