package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughPointException;

public interface PointManagement {

    int addPoints(int numberOfPoints, Card card);

    int retrievePoints(int numberOfPoints, Card card) throws NotEnoughPointException;

}
