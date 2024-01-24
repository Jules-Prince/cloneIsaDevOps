package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughBalanceException;

public interface Pay {

    Purchase createPurchasePayWithCard(Purchase purchase) throws NotEnoughBalanceException;

    Purchase createPurchase(Purchase purchase) ;
}

