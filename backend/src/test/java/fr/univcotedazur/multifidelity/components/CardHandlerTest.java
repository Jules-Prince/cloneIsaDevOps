package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.connectors.BankProxy;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.BalanceAmountUnauthorizedException;
import fr.univcotedazur.multifidelity.exceptions.BankRefusedPaymentException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
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
public class CardHandlerTest {


    @Autowired
    private CardHandler cardHandler;

    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private ConsumerRepository consumerRepository;

    @Mock
    private BankProxy bank;
    private Card card;

    private Consumer consumer;


    @BeforeEach
    public void setUpContext() {
        card = new Card(0,20);
        consumer = new Consumer("djulo.fr@bg.com", "fleur1234", "123456789", "YOOO", null);
        consumer =consumerRepository.save(consumer);
        ((CardManager)cardHandler.getBalanceManagement()).setBank(bank);
    }


    @AfterEach
    public void cleanUpContext() throws Exception{
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
    }


    @Test
    public void createCardTest() throws AlreadyExistSimilarRessourceException {
        Card card1 =cardHandler.createCard(consumer);
        assertEquals(1, cardRepository.findAll().stream().toList().size());
        assertEquals(1, consumerRepository.findAll().stream().toList().size());
        assertEquals(consumer.getCard(),card1);
        assertEquals(0,card1.getPoint());
        assertEquals(0,card1.getBalance());
    }

    @Test
    @Transactional(propagation= Propagation.NEVER)
    public void createCardTestAlreadyExistRessourceException() throws AlreadyExistSimilarRessourceException {
        cardHandler.createCard(consumer);
        Assertions.assertThrows(AlreadyExistSimilarRessourceException.class, () ->  cardHandler.createCard(consumer));
    }

    @Test
    public void getPointTest() {
        cardHandler.createCard(consumer);
        assertEquals(cardHandler.getPoint(consumer),0);
   }


    @Test
    public void getPointTest2() {
        fr.univcotedazur.multifidelity.entities.Card card1 =cardHandler.createCard(consumer);
        card1.setPoint(12);
        cardRepository.save(card1);
        assertEquals(cardHandler.getPoint(consumer),12);

    }

    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void getPointTestResourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () ->  cardHandler.getPoint(consumer));
    }

    @Test
    public void reloadBalanceTest() throws ResourceNotFoundException, BankRefusedPaymentException, BalanceAmountUnauthorizedException {
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        when(bank.pay(consumer, 8)).thenReturn(true);
        Float balance = cardHandler.reloadBalance(8,consumer);
        assertEquals(28, balance);

    }

    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void reloadBalanceTestBalanceAmountUnauthorizedException() throws ResourceNotFoundException, BankRefusedPaymentException, BalanceAmountUnauthorizedException {
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        when(bank.pay(consumer, 100)).thenReturn(false);
        Assertions.assertThrows(BalanceAmountUnauthorizedException.class, () ->  cardHandler.reloadBalance(100,consumer));
    }

    @Test
    public void getBalanceTest() throws ResourceNotFoundException {
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        assertEquals(cardHandler.getBalance(consumer),20);
    }

    @Test
    public void getBalanceAfterReloadTest() throws ResourceNotFoundException, BankRefusedPaymentException, BalanceAmountUnauthorizedException {
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        when(bank.pay(consumer, 8)).thenReturn(true);
        cardHandler.reloadBalance(8,consumer);
        assertEquals(cardHandler.getBalance(consumer),28);
    }


    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void getBalanceAfterTooMuchReloadTest() throws ResourceNotFoundException, BankRefusedPaymentException, BalanceAmountUnauthorizedException {
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        when(bank.pay(consumer, 100)).thenReturn(false);
        Assertions.assertThrows(BalanceAmountUnauthorizedException.class, () ->  cardHandler.reloadBalance(100,consumer));
    }



}
