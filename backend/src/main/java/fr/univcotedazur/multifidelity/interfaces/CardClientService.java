package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.BalanceAmountUnauthorizedException;
import fr.univcotedazur.multifidelity.exceptions.BankConnexionException;
import fr.univcotedazur.multifidelity.exceptions.BankRefusedPaymentException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

public interface CardClientService {

    Card createCard(Consumer consumer) throws AlreadyExistSimilarRessourceException;

    int getPoint(Consumer consumer) throws ResourceNotFoundException;

    float reloadBalance(float amount, Consumer consumer) throws BankRefusedPaymentException, BalanceAmountUnauthorizedException, BankConnexionException;


    float getBalance(Consumer consumer);


}
