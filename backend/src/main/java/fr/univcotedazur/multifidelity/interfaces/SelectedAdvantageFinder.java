package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;

import java.util.List;
import java.util.Optional;

public interface SelectedAdvantageFinder {

    Optional<SelectedAdvantage> findSelectedAdvantageById(Long id);

    List<SelectedAdvantage> getAllSelectedAdvantages();
}
