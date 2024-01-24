package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;

public interface CatalogModifier {

    Advantage createAdvantage(Advantage advantage) throws AlreadyExistSimilarRessourceException;
    void deleteAdvantage(Advantage advantage);

    Advantage updateAdvantage(Advantage oldAdvantage, Advantage newAdvantage);
}
