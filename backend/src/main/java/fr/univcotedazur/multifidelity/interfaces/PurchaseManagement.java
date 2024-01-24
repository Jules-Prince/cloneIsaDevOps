package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

public interface PurchaseManagement {

    Purchase addPurchase(Purchase purchase) throws ResourceNotFoundException;
}

