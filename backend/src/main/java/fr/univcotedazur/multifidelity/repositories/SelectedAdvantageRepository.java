package fr.univcotedazur.multifidelity.repositories;

import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface SelectedAdvantageRepository extends JpaRepository<SelectedAdvantage, Long> {

}
