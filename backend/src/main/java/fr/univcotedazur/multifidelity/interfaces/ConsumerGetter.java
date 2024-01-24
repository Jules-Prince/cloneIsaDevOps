package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

import java.util.List;

public interface ConsumerGetter {

    Consumer getConsumerById(Long id) throws ResourceNotFoundException;


    List<Consumer> getAllConsumer();

    List<Society> getFavSocietiesByConsumer(Consumer consumer) throws ResourceNotFoundException;

    Consumer getConsumerByPlate(String plate);
}
