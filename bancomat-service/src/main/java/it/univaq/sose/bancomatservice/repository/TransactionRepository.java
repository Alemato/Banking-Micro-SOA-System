package it.univaq.sose.bancomatservice.repository;

import it.univaq.sose.bancomatservice.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findDistinctByBancomat_AccountIdOrderByCreateDateDesc(Long accountId);
}