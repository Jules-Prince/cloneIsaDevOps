package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Society;

public interface StatisticGiver {

    float getProfitBySociety(Society society) ;

    float getProfit();

    float getFrequencyPurchaseBySociety(Society society);

    float getFrequencyPurchase();

}
