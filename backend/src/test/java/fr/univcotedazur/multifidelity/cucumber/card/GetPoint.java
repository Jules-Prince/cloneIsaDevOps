package fr.univcotedazur.multifidelity.cucumber.card;

import fr.univcotedazur.multifidelity.components.CardHandler;
import fr.univcotedazur.multifidelity.components.Payment;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetPoint {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private SocietyRepository societyRepository;


    @Autowired
    private CardHandler cardHandler;

    @Autowired
    private Payment paymentService;

    private Card card;

    private Consumer consumer;

    private Consumer consumer2;

    private Purchase purchase;

    private Society society;

    private Long cardId;

    private Long consumerId;

    @Before
    public void cleanUpContext() throws Exception{
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @Given("a new consumer with a card ,an another consumer without card")
    public void aNewConsumerWithACardAnAnotherConsumerWithoutCard() {
        card = new Card(0,20);
        cardId = cardRepository.save(card).getId();
        card =cardHandler.getCardById(cardId);
        consumer = new Consumer();
        consumer.setCard(card);
        consumer2 = new Consumer();
        consumerId =consumerRepository.save(consumer).getId();
        consumerRepository.save(consumer2);
        society = new Society();
        society = societyRepository.save(society);
    }

    @Then("the number of point is {int}")
    public void theNumberOfPointIs(int arg0) {
        consumer = consumerRepository.findById(consumerId).get();
        assertEquals(arg0, cardHandler.getPoint(consumer));

    }

    @Transactional(propagation= Propagation.NEVER)
    @Then("have failure")
    public void haveFailure() {
        consumer = consumerRepository.findById(consumerId).get();
        Assertions.assertThrows(ResourceNotFoundException.class, () ->   cardHandler.getPoint(consumer2));
    }

    @When("the consumer make a purchase at {int} price")
    public void theConsumerMakeAPurchaseAtPrice(int arg0) {
        card =cardHandler.getCardById(cardId);
        consumer = consumerRepository.findById(consumerId).get();
        purchase = new Purchase((float) arg0, LocalDateTime.now(),society,card);
        paymentService.createPurchasePayWithCard(purchase);
    }
}
