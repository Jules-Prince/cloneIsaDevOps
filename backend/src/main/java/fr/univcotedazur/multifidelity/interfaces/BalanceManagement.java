package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.BalanceAmountUnauthorizedException;
import fr.univcotedazur.multifidelity.exceptions.BankConnexionException;
import fr.univcotedazur.multifidelity.exceptions.BankRefusedPaymentException;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughBalanceException;

public interface BalanceManagement {


    float addBalance(float amount, Card card, Consumer consumer) throws BankRefusedPaymentException, BalanceAmountUnauthorizedException, BankConnexionException;


    float retrieveBalance(float amount, Card card) throws NotEnoughBalanceException;

    float getBalance(Card card);
}
