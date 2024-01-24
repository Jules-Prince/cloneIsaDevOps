package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

public interface CardGetter {

    Card getCardById(Long id) throws ResourceNotFoundException;

}
