package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Card;

import java.util.List;
import java.util.Optional;


public interface CardFinder {

    Optional<Card> findCardById(Long id);


    List<Card> getAllCards();
}
