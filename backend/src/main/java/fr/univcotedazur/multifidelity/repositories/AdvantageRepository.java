package fr.univcotedazur.multifidelity.repositories;

import fr.univcotedazur.multifidelity.entities.Advantage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface AdvantageRepository extends JpaRepository<Advantage, Long> {

}
