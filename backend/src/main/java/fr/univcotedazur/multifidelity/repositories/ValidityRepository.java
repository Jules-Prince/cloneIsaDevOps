package fr.univcotedazur.multifidelity.repositories;

import fr.univcotedazur.multifidelity.entities.Validity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidityRepository extends JpaRepository<Validity, Long> {
}
