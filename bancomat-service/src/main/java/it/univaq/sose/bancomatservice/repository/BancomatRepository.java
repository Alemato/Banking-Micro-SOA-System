package it.univaq.sose.bancomatservice.repository;

import it.univaq.sose.bancomatservice.domain.Bancomat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BancomatRepository extends JpaRepository<Bancomat, Long> {
    Optional<Bancomat> findByAccountId(Long accountId);
}