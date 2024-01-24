package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

import java.util.List;

public interface AdvantageGetter {

    Advantage getAdvantageById(Long id) throws ResourceNotFoundException;

    List<Advantage> getAllAdvantages();

    List<Advantage> getAllAdvantagesBySociety(Society society) ;
    List<Advantage> getAllAdvantageEligibleByConsumer(Consumer consumer);



}
