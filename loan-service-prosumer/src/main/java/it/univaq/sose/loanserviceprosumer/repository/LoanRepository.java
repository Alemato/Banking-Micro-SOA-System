package it.univaq.sose.loanserviceprosumer.repository;

import it.univaq.sose.loanserviceprosumer.domain.Loan;
import it.univaq.sose.loanserviceprosumer.domain.dto.LoanDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT new it.univaq.sose.loanserviceprosumer.domain.dto.LoanDto(" +
            "l.id, l.amount, l.interestRate, l.termInYears, l.borrowerName, " +
            "l.loanStatus, l.idBankAccount, l.idAccount, l.createDate, l.updateDate) " +
            "FROM Loan l WHERE l.idBankAccount = :idBankAccount ORDER BY l.createDate DESC")
    List<LoanDto> findByIdBankAccountOrderByCreateDateDesc(@Param("idBankAccount") Long idBankAccount);

//    List<LoanDto> findByIdBankAccountOrderByCreateDateDesc(Long idBankAccount);


}