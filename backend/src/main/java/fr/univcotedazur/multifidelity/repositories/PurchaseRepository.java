package fr.univcotedazur.multifidelity.repositories;

import fr.univcotedazur.multifidelity.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}