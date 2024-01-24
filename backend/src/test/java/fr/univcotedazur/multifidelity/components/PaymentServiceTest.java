package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughBalanceException;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Commit
public class PaymentServiceTest {


    @Autowired
    private Payment paymentService;


    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private ConsumerRepository consumerRepository;


    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private SocietyRepository societyRepository;

    private Card card;

    private Consumer consumer;

    private Purchase purchase;

    private Society society;

    private Long cardId;


    @BeforeEach
    public void setUpContext() {
        card = new Card(0,20);
        cardId = cardRepository.save(card).getId();
        card = cardRepository.findById(cardId).get();
        consumer = new Consumer();
        consumer.setCard(card);
        card.setConsumer(consumer);
        consumer = consumerRepository.save(consumer);
        society = new Society();
        society = societyRepository.save(society);
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
    public void buyInSocietyWithFidelityCardTest() throws NotEnoughBalanceException {
        paymentService.createPurchasePayWithCard(purchase);
        assertEquals(8, purchase.getCard().getBalance());
        assertEquals(6,purchase.getCard().getPoint());
    }

    @Test
    public void buyInSocietyTest()  {
        paymentService.createPurchase(purchase);
        assertEquals(6,purchase.getCard().getPoint());
    }


    @Test
    public void getAllPurchasesBySocietyTest() {
        paymentService.createPurchase(purchase);
        assertEquals(List.of(purchase),paymentService.getAllPurchasesBySociety(society));
    }


    @Test
    public void getAllPurchasesByConsumerTest() {
        paymentService.createPurchase(purchase);
        assertEquals(List.of(purchase),paymentService.getAllPurchasesByConsumer(consumer));
    }

}
