package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.BalanceAmountUnauthorizedException;
import fr.univcotedazur.multifidelity.exceptions.BankConnexionException;
import fr.univcotedazur.multifidelity.exceptions.BankRefusedPaymentException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.BalanceManagement;
import fr.univcotedazur.multifidelity.interfaces.CardClientService;
import fr.univcotedazur.multifidelity.interfaces.CardCreator;
import fr.univcotedazur.multifidelity.interfaces.CardFinder;
import fr.univcotedazur.multifidelity.interfaces.CardGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CardHandler implements CardClientService , CardGetter {

    @Autowired
    private CardFinder cardFinder;

    @Autowired
    private CardCreator cardCreator;

    @Autowired
    private BalanceManagement balanceManagement;


    @Override
    public Card createCard(Consumer consumer) throws AlreadyExistSimilarRessourceException {
        boolean alreadyHaveCard = consumer.getCard()!=null;
        if(alreadyHaveCard){
            throw new AlreadyExistSimilarRessourceException("the user already has a loyalty card");
        }
        Card card = cardCreator.addCard(new Card(0,0));
        consumer.setCard(card);
        return card;
    }

    @Override
    public int getPoint(Consumer consumer) throws ResourceNotFoundException{
        if(consumer.getCard()==null){
            throw new ResourceNotFoundException("no card found for this consumer");
        }
        return consumer.getCard().getPoint();
    }

    @Override
    public Card getCardById(Long id) throws ResourceNotFoundException {
        return cardFinder.findCardById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("No card found for id: %s", id.toString())));
    }


    @Override
    public float reloadBalance(float amount, Consumer consumer) throws BankRefusedPaymentException, BalanceAmountUnauthorizedException, BankConnexionException {
        if(consumer.getCard()==null){
            throw new ResourceNotFoundException("no card found for this consumer");
        }
         return balanceManagement.addBalance(amount,consumer.getCard(),consumer);
    }

    @Override
    public float getBalance(Consumer consumer) {
        if(consumer.getCard()==null){
            throw new ResourceNotFoundException("no card found for this consumer");
        }
        return balanceManagement.getBalance(consumer.getCard());
    }

    public BalanceManagement getBalanceManagement() {
        return balanceManagement;
    }
}
