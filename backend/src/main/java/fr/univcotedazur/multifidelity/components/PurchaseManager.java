package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.PurchaseAnalyst;
import fr.univcotedazur.multifidelity.interfaces.PurchaseManagement;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseManager implements PurchaseManagement, PurchaseAnalyst {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public Purchase addPurchase(Purchase purchase) {
        purchase.setDate(LocalDateTime.now());

        return purchaseRepository.save(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchases() {
        return new ArrayList<>(purchaseRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesBySociety(Society society) {
        return purchaseRepository.findAll().stream().filter(purchase -> purchase.getSociety()==society).collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesByConsumer(Consumer consumer) {
        return purchaseRepository.findAll().stream().filter(purchase -> purchase.getCard()==consumer.getCard()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Purchase getPurchaseById(Long id) throws ResourceNotFoundException {
        return purchaseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("No purchase found for id: %s", id.toString())));
    }
}
