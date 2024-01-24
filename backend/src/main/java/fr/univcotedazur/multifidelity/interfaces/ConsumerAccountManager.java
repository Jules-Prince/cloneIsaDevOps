package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AccountNotFoundException;

public interface ConsumerAccountManager {


    Consumer researchConsumer(String email , String password) throws AccountNotFoundException;

    Consumer addConsumer(Consumer newConsumer) ;

    Consumer updateConsumer(Consumer oldConsumer, Consumer newConsumer) ;

    Consumer addFavSociety(Society society, Consumer consumer);

    Consumer deleteFavSociety(Society society, Consumer consumer);

    Consumer setVfp(Consumer consumer,boolean isVfp);

    void deleteAllConsumer();
}
