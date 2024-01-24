package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;

public interface SocietyModifier {

    Society updateSocietyInformation(Society oldSociety,Society newSociety);

    Society addSociety(Society newSociety) throws AlreadyExistSimilarRessourceException;

}

