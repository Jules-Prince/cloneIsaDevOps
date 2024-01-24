package fr.univcotedazur.multifidelity.cucumber.purchase;

import fr.univcotedazur.multifidelity.components.Payment;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class GetPurchaseBySociety {

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


    @Autowired
    private PartnerRepository partnerRepository;

    private Card card;

    private Consumer consumer;

    private Long purchaseId;


    private Card card2;

    private Consumer consumer2;

    private Long purchase2Id;


    private Long societyId;

    private Partner partner;


    private Long cardId;

    private Long card2Id;

    @AfterEach
    public void cleanUpContext() throws Exception{
        purchaseRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
    }

    @Given("{int} consumers, his cards and a society")
    public void consumersHisCardsAndASociety(int arg0) {
        card = new Card(12, (float) 12);
        card2 = new Card(12, (float) 12);
        cardId = cardRepository.save(card).getId();
        card = cardRepository.findById(cardId).get();
        card2Id =cardRepository.save(card2).getId();
        consumer = new Consumer("email", "password", null, null,card);
        consumer2 = new Consumer("email2", "password2", null, null,card2);
        consumerRepository.save(consumer);
        consumerRepository.save(consumer2);
        partner = partnerRepository.save(new Partner());
        Society society = new Society( "Boulangerie2", partner, LocalTime.of(9,0), LocalTime.of(18,0), "73 rue blibli");
        societyId =societyRepository.save(society).getId();
    }

    @When("the consumers make a purchase in society")
    public void theConsumersMakeAPurchaseInSociety() {
        Society society = societyRepository.findById(societyId).get();
        card = cardRepository.findById(cardId).get();
        Purchase purchase = new Purchase((float) 2, LocalDateTime.now(),society,card);
        purchaseId = paymentService.createPurchasePayWithCard(purchase).getId();

        card2 = cardRepository.findById(card2Id).get();
        Purchase purchase2 = new Purchase((float) 2,null,society,card2);
        purchase2Id = paymentService.createPurchasePayWithCard(purchase2).getId();
    }

    @Then("there is {int} purchase for the society")
    public void thereIsPurchaseForTheSociety(int arg0) {
        Society society = societyRepository.findById(societyId).get();
        assertEquals(arg0,  paymentService.getAllPurchasesBySociety(society).size());
    }


    @Then("this is this purchase  for the society")
    public void thisIsThisPurchaseForTheSociety() {
        Purchase purchase = purchaseRepository.findById(purchaseId).get();
        Purchase purchase2 = purchaseRepository.findById(purchase2Id).get();
        Society society = societyRepository.findById(societyId).get();
        assertTrue(paymentService.getAllPurchasesBySociety(society).contains(purchase));
        assertTrue(paymentService.getAllPurchasesBySociety(society).contains(purchase2));
    }
}
