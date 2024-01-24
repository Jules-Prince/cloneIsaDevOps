package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;

public interface ConsumerModifier {

    Consumer updateConsumer(Consumer oldConsumer,Consumer newConsumer);


    Consumer addFavSociety(Society society, Consumer consumer);

    Consumer deleteFavSociety(Society society, Consumer consumer);


    void deleteAllConsumer();

    Consumer setVfp(Consumer consumer,boolean isVfp);

}
