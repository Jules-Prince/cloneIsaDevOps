package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

import java.util.List;

public interface PurchaseAnalyst {

    List<Purchase> getAllPurchases();

    Purchase getPurchaseById(Long id) throws ResourceNotFoundException;

    List<Purchase> getAllPurchasesBySociety(Society society);

    List<Purchase> getAllPurchasesByConsumer(Consumer consumer);

}
