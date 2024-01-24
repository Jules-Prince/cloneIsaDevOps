package fr.univcotedazur.multifidelity.repositories;

import fr.univcotedazur.multifidelity.entities.Society;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface SocietyRepository extends JpaRepository<Society, Long> {

}