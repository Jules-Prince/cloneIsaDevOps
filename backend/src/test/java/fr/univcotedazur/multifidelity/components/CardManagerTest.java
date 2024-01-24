package fr.univcotedazur.multifidelity.components;


import fr.univcotedazur.multifidelity.connectors.BankProxy;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.BalanceAmountUnauthorizedException;
import fr.univcotedazur.multifidelity.exceptions.BankRefusedPaymentException;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughBalanceException;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughPointException;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@Commit
@Transactional
public class CardManagerTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private CardManager cardManager;

    @Mock
    private BankProxy bank;


    private Card card;

    private Consumer consumer;


    @BeforeEach
    public void setUpContext() {
        card = new Card(0,0);
        card = cardRepository.save(card);
        consumer = new Consumer("djulo.fr@bg.com", "fleur1234", "123456789", "YOOO", null);
        consumer.setCard(card);
        consumer =consumerRepository.save(consumer);
        cardManager.setBank(bank);
    }

    @AfterEach
    public void cleanUpContext() throws Exception{
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @Test
    public void addBalanceTest() throws BankRefusedPaymentException, BalanceAmountUnauthorizedException {
        when(bank.pay(consumer, 7)).thenReturn(true);
        assertEquals(7,  cardManager.addBalance(7,card,consumer));
        assertEquals(7,  cardManager.findCardById(card.getId()).get().getBalance());
        when(bank.pay(consumer, 2)).thenReturn(true);
        assertEquals(9,  cardManager.addBalance(2,card,consumer));
    }

    @Test
    @Transactional(propagation= Propagation.NEVER)
    public void addBalanceTestBalanceAmountUnauthorizedException() throws BankRefusedPaymentException  {
        Assertions.assertThrows(BalanceAmountUnauthorizedException.class, () ->  cardManager.addBalance(101,card,consumer));
    }


    @Test
    public void retrieveBalanceTest() throws NotEnoughBalanceException, BankRefusedPaymentException, BalanceAmountUnauthorizedException {
        when(bank.pay(consumer, 7)).thenReturn(true);
        cardManager.addBalance(7,card,consumer);
        assertEquals(5,   cardManager.retrieveBalance(2,card));
        assertEquals(5,  cardManager.findCardById(card.getId()).get().getBalance());
    }


    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void retrieveBalanceTestNotEnoughBalanceException() throws BankRefusedPaymentException, BalanceAmountUnauthorizedException {
        when(bank.pay(consumer, 7)).thenReturn(true);
        cardManager.addBalance(7,card,consumer);
        Assertions.assertThrows(NotEnoughBalanceException.class, () ->  cardManager.retrieveBalance(10,card));
    }

    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void retrieveBalanceTestBalanceInvalidDueToErrorWithBankProxy() {
        when(bank.pay(consumer, 7)).thenReturn(true);
        consumer = new Consumer( "djulo.fr@bg.com", "fleur1234", "coucou-jecasse-TOUUUUT", "YOOO", null);
        card = new Card(0,0);
        Assertions.assertThrows(BankRefusedPaymentException.class, () -> cardManager.addBalance(7,card,consumer));
    }


    @Test
    public void addPointsTest()  {
        assertEquals(2,   cardManager.addPoints(2,card));
        assertEquals(2,  cardManager.findCardById(card.getId()).get().getPoint());
        assertEquals(4,   cardManager.addPoints(2,card));
    }


    @Test
    public void retrievePointsTest() throws NotEnoughPointException {
       cardManager.addPoints(2,card);
        assertEquals(1,   cardManager.retrievePoints(1,card));
        assertEquals(1,  cardManager.findCardById(card.getId()).get().getPoint());
    }


    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void retrievePointsTestNotEnoughPointException() throws NotEnoughPointException {
        cardManager.addPoints(2,card);
        Assertions.assertThrows(NotEnoughPointException.class, () -> cardManager.retrievePoints(10,card));
    }

    @Test
    @Transactional
    public void addCardTest()  {
        card = new Card(0,20);
        cardManager.addCard(card);
        assertEquals(2, cardRepository.findAll().stream().toList().size());
    }




}
