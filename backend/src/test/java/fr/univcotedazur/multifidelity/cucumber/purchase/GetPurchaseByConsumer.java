package fr.univcotedazur.multifidelity.cucumber.purchase;

import fr.univcotedazur.multifidelity.components.Payment;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetPurchaseByConsumer {

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

    private Long consumerId;

    private Long purchaseId;

    private Society society;

    private Long cardId;

    @Before
    public void beforeContext() throws Exception{
        purchaseRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @AfterEach
    public void cleanUpContext() throws Exception{
        purchaseRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @Given("a consumer, his card and a society")
    public void aConsumerHisCardAndASociety() {
        card = new Card(12, (float) 12);
        cardId  =cardRepository.save(card).getId();
        card = cardRepository.findById(cardId).get();
        Consumer consumer = new Consumer("email", "password", null, null,card);
        consumerId =consumerRepository.save(consumer).getId();
        society = new Society();
        society =societyRepository.save(society);
    }

    @When("make a purchase in society")
    public void makeAPurchaseInSociety() {
        card = cardRepository.findById(cardId).get();
        Purchase purchase = new Purchase((float) 2,null,society,card);
        purchaseId = paymentService.createPurchasePayWithCard(purchase).getId();
    }

    @Then("there is one purchase")
    public void thereIsOnePurchase() {
        assertEquals(1,  paymentService.getAllPurchasesByConsumer(consumerRepository.findById(consumerId).get()).size());
    }

    @Then("this is this purchase")
    public void thisIsThisPurchase() {
        Purchase purchase = purchaseRepository.findById(purchaseId).get();
        assertEquals(List.of(purchase),paymentService.getAllPurchasesByConsumer(consumerRepository.findById(consumerId).get()));
    }
}
