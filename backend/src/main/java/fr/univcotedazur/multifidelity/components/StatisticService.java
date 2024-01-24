package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.interfaces.PurchaseAnalyst;
import fr.univcotedazur.multifidelity.interfaces.SelectedAdvantageFinder;
import fr.univcotedazur.multifidelity.interfaces.StatisticGiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
public class StatisticService implements StatisticGiver {

    @Autowired
    private PurchaseAnalyst purchaseAnalyst;


    @Autowired
    private SelectedAdvantageFinder selectedAdvantageFinder;


    @Override
    public float getProfitBySociety(Society society) {
        List<Purchase> purchases = purchaseAnalyst.getAllPurchasesBySociety(society);
        List<SelectedAdvantage> selectedAdvantages = selectedAdvantageFinder.getAllSelectedAdvantages().stream().filter(selectedAdvantage -> selectedAdvantage.getAdvantage().getSociety().equals(society)).toList();
        float amountPurchases = 0;
        float amountGift = 0;
        for (Purchase purchase : purchases) {
            amountPurchases += purchase.getAmount();
        }
        for (Advantage advantage: selectedAdvantages.stream().map(SelectedAdvantage::getAdvantage).toList()) {
            amountPurchases += advantage.getPrice();
            amountGift += advantage.getInitialPrice() - advantage.getPrice();

        }
        return amountPurchases - amountGift;
    }

    @Override
    public float getProfit() {
        List<Purchase> purchases = purchaseAnalyst.getAllPurchases();
        List<SelectedAdvantage> selectedAdvantages = selectedAdvantageFinder.getAllSelectedAdvantages();
        float amountPurchases = 0;
        float amountGift = 0;
        for (Purchase purchase : purchases) {
            amountPurchases += purchase.getAmount();
        }
        for (Advantage advantage: selectedAdvantages.stream().map(SelectedAdvantage::getAdvantage).toList()) {
            amountPurchases += advantage.getPrice();
            amountGift += advantage.getInitialPrice() - advantage.getPrice();
        }
        return amountPurchases - amountGift;
    }

    @Override
    public float getFrequencyPurchaseBySociety(Society society) {
        LocalDateTime now = LocalDateTime.now().plusHours(1);
        LocalDateTime weekAgo = now.minusWeeks(1);
        return (purchaseAnalyst.getAllPurchasesBySociety(society).stream().filter(purchase ->
                purchase.getDate().isAfter(weekAgo) && purchase.getDate().isBefore(now)).count())/7f;


    }

    @Override
    public float getFrequencyPurchase() {
        LocalDateTime now = LocalDateTime.now().plusHours(1);
        LocalDateTime weekAgo = now.minusWeeks(1);
        return (purchaseAnalyst.getAllPurchases().stream().filter(purchase ->
                purchase.getDate().isAfter(weekAgo) && purchase.getDate().isBefore(now)).count())/7f;
    }
}
