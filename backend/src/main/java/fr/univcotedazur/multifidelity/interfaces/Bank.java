package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.BankConnexionException;

public interface Bank {

    boolean pay(Consumer consumer, float value) throws BankConnexionException;

}
