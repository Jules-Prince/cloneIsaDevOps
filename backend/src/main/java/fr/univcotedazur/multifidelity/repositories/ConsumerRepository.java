package fr.univcotedazur.multifidelity.repositories;

import fr.univcotedazur.multifidelity.entities.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    Optional<Consumer> findByLicencePlate(String plate);

    Optional<Consumer> findByEmail(String email);
}
