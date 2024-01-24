package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughBalanceException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.BalanceManagement;
import fr.univcotedazur.multifidelity.interfaces.Pay;
import fr.univcotedazur.multifidelity.interfaces.PointManagement;
import fr.univcotedazur.multifidelity.interfaces.PurchaseAnalyst;
import fr.univcotedazur.multifidelity.interfaces.PurchaseGetter;
import fr.univcotedazur.multifidelity.interfaces.PurchaseManagement;
import fr.univcotedazur.multifidelity.interfaces.VfpChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class Payment implements Pay, PurchaseGetter {

    @Autowired
    private VfpChecker vfpChecker;

    @Autowired
    private  BalanceManagement balanceManagement;

    @Autowired
    private  PointManagement pointManagement;

    @Autowired
    private PurchaseManagement purchaseManagement;

    @Autowired
    private PurchaseAnalyst purchaseAnalyst;



    @Override
    public Purchase createPurchasePayWithCard(Purchase purchase) throws NotEnoughBalanceException {
        Card card = purchase.getCard();
        balanceManagement.retrieveBalance(purchase.getAmount(),card);
        pointManagement.addPoints((int)purchase.getAmount()/2,purchase.getCard());
        Purchase newPurchase = purchaseManagement.addPurchase(purchase);
        vfpChecker.VFPCheckByConsumer( card.getConsumer());
        return newPurchase;
    }

    @Override
    public Purchase createPurchase(Purchase purchase) {
        Card card = purchase.getCard();
        pointManagement.addPoints((int)purchase.getAmount()/2,card);
        Purchase newPurchase = purchaseManagement.addPurchase(purchase);
        vfpChecker.VFPCheckByConsumer(card.getConsumer());
        return newPurchase;
    }

    @Override
    public Purchase getPurchaseById(Long id) throws ResourceNotFoundException {
        return purchaseAnalyst.getPurchaseById(id);
    }

    @Override
    public List<Purchase> getAllPurchasesBySociety(Society society) {
        return purchaseAnalyst.getAllPurchasesBySociety(society);
    }


    @Override
    public List<Purchase> getAllPurchasesByConsumer(Consumer consumer) {
        return purchaseAnalyst.getAllPurchasesByConsumer(consumer);
    }
}
