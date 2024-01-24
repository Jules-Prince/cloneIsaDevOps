package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.BalanceAmountUnauthorizedException;
import fr.univcotedazur.multifidelity.exceptions.BankConnexionException;
import fr.univcotedazur.multifidelity.exceptions.BankRefusedPaymentException;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughBalanceException;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughPointException;
import fr.univcotedazur.multifidelity.interfaces.BalanceManagement;
import fr.univcotedazur.multifidelity.interfaces.Bank;
import fr.univcotedazur.multifidelity.interfaces.CardCreator;
import fr.univcotedazur.multifidelity.interfaces.CardFinder;
import fr.univcotedazur.multifidelity.interfaces.PointManagement;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(noRollbackFor=NotEnoughPointException.class)
public class CardManager implements BalanceManagement, PointManagement, CardFinder, CardCreator {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private  Bank bank;

    @Override
    public float addBalance(float amount, Card card, Consumer consumer) throws BankRefusedPaymentException, BalanceAmountUnauthorizedException, BankConnexionException {
        float newBalance =  card.getBalance() + amount;
        if(newBalance> 100){
            throw new BalanceAmountUnauthorizedException("the authorized balance of the card will be exceeded by this additional balance");
        }
        boolean status = bank.pay(consumer, amount);
        if(!status){
            throw new BankRefusedPaymentException("The bank refused the payment");
        }
        card.setBalance(newBalance);
        cardRepository.save(card);
        return card.getBalance();
    }

    @Override
    public Card addCard(Card card)  {
        return cardRepository.save(card);
    }

    @Override
    public float retrieveBalance(float amount, Card card) throws NotEnoughBalanceException {
        boolean canBuy = canBuy(amount,card);
        if(!canBuy){
            throw new NotEnoughBalanceException("Not enough balance on the card to pay");
        }
        card.setBalance(card.getBalance() - amount);
        cardRepository.save(card);
        return card.getBalance();
    }

    @Override
    public float getBalance(Card card) {
        return card.getBalance();
    }


    private boolean canBuy(float amount, Card card) {
        float balance = card.getBalance();
        float newBalance = balance - amount;
        return newBalance >= 0;
    }


    public int addPoints(int numberOfPoints, Card card) {
        card.setPoint(numberOfPoints + card.getPoint());
        cardRepository.save(card);
        return card.getPoint();
    }

    @Override
    public int retrievePoints(int numberOfPoints, Card card) throws NotEnoughPointException {
        int points = card.getPoint();
        int newNumberOfPoints = points - numberOfPoints;
        if(newNumberOfPoints<0){
            throw new NotEnoughPointException("The amount of point is negative");
        }
        card.setPoint(newNumberOfPoints);
        return card.getPoint();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Card> findCardById(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> getAllCards() {
        return new ArrayList<>(cardRepository.findAll());
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
