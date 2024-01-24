package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Commit
public class PurchaseManagerTest {

    @Autowired
    private PurchaseRepository purchaseRepository;


    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private ConsumerRepository consumerRepository;



    @Autowired
    private SocietyRepository societyRepository;


    @Autowired
    private PurchaseManager purchaseManager;


    private Card card;

    private Purchase purchase;

    private Consumer consumer;

    private Society society;


    @BeforeEach
    public void setUpContext() {
        card = new Card(0,0);
        card =  cardRepository.save(card);
        consumer = new Consumer("claire@orange.com", "paswd", "licence","card",null);
        consumer.setCard(card);
        consumer =consumerRepository.save(consumer);
        society = new Society();
        society =societyRepository.save(society);
        purchase = new Purchase(12, LocalDateTime.now(),society,card);
    }

    @AfterEach
    public void cleanUpContext() throws Exception{
        purchaseRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @Test
    public void addPurchaseTest()  {
        purchaseManager.addPurchase(purchase);
        assertEquals(1,  purchaseRepository.count());
    }

    @Test
    public void getAllPurchasesTest() {
        purchaseManager.addPurchase(purchase);
        assertEquals(List.of(purchase),purchaseManager.getAllPurchases());
    }


    @Test
    public void getAllPurchasesBySocietyTest() {
        purchaseManager.addPurchase(purchase);
        assertEquals(List.of(purchase),purchaseManager.getAllPurchasesBySociety(society));
    }


    @Test
    public void getPurchaseByIdTest() {
        Purchase newPurchase = purchaseManager.addPurchase(purchase);
        assertEquals(newPurchase,purchaseManager.getPurchaseById(newPurchase.getId()));
    }

    @Test
    @Transactional(propagation= Propagation.NEVER)
    public void getPurchaseByIdTestResourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            purchaseManager.getPurchaseById(125L);
        });
    }


    @Test
    public void getAllPurchasesByConsumerTest() {
        purchaseManager.addPurchase(purchase);
        assertEquals(List.of(purchase),purchaseManager.getAllPurchasesByConsumer(consumer));
    }

}
