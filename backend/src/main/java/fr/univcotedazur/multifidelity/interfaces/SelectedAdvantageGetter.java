package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

import java.util.List;

public interface SelectedAdvantageGetter {

    List<SelectedAdvantage> getAllSelectedAdvantageEligibleByConsumer(Consumer consumer);

    List<SelectedAdvantage> getAllSelectedAdvantages();


    SelectedAdvantage getSelectedAdvantageById(Long id) throws ResourceNotFoundException;
}
