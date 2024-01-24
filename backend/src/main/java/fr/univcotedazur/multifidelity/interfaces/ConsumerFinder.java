package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Consumer;

import java.util.List;
import java.util.Optional;

public interface ConsumerFinder {

    Optional<Consumer> findConsumerById(Long id);

    Optional<Consumer> findConsumerByEmail(String email);

    List<Consumer> getAllConsumer();

    Optional<Consumer> findConsumerByPlate(String plate);
}
