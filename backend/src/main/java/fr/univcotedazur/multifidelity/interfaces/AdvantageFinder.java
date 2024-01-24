package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Society;

import java.util.List;
import java.util.Optional;

public interface AdvantageFinder {

    Optional<Advantage> findAdvantageById(Long id);


    List<Advantage> getAllAdvantages();


    List<Advantage> getAllAdvantageBySociety(Society society);


    Optional<Advantage> findAdvantageByNameBySociety(String name, Society society);
}
