package it.univaq.sose.loanserviceprosumer.repository;

import it.univaq.sose.loanserviceprosumer.domain.Loan;
import it.univaq.sose.loanserviceprosumer.domain.dto.LoanDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<LoanDto> findByIdBankAccountOrderByCreateDateDesc(Long idBankAccount);
}