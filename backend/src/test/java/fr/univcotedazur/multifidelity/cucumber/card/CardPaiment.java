package fr.univcotedazur.multifidelity.cucumber.card;

import fr.univcotedazur.multifidelity.components.CardHandler;
import fr.univcotedazur.multifidelity.components.Payment;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughBalanceException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
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
public class CardPaiment {

    @Autowired
    private Payment paymentService;


    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CardHandler cardHandler;

    private Long cardId;

    private Consumer consumer;

    private Purchase purchase;

    private Society society;

    @Before
    public void cleanUpContext() throws Exception{
        purchaseRepository.deleteAll();
        cardRepository.deleteAll();
        consumerRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @Given("customer with a loyalty card and a balance of {double} euros and a point balance of {int} points")
    public void customerWithALoyaltyCardAndABalanceOfEurosAndAPointBalanceOfPoints(double arg0, int arg1) {
        consumer = new Consumer();
        Card card =cardRepository.save(new Card(arg1, (float) arg0));
        consumer.setCard(card);
        cardId = card.getId();
        consumerRepository.save(consumer);
        society = new Society();
        society =societyRepository.save(society);
    }

    @When("the customer goes to the cashier to pay an amount of {double} euros")
    public void theCustomerGoesToTheCashierToPayAnAmountOfEuros(double arg0) throws NotEnoughBalanceException {
        Card card1 = cardHandler.getCardById(cardId);
        purchase = new Purchase((float) arg0, LocalDateTime.now(),society,card1);
        paymentService.createPurchasePayWithCard(purchase);
    }

    @Then("the customer's balance increases to {double} euros")
    public void theCustomerSBalanceIncreasesToEuros(double arg0) throws ResourceNotFoundException {
        Card card1 = cardHandler.getCardById(cardId);
        assertEquals(arg0, card1.getBalance());
    }

    @Then("its number of points is {int} points")
    public void itsNumberOfPointsIsPoints(int arg0) throws ResourceNotFoundException {
        Card card1 = cardHandler.getCardById(cardId);
        assertEquals(arg0,card1.getPoint());
    }

    @Transactional(propagation= Propagation.NEVER)
    @Then("the customer goes to the cashier to pay an amount of {double} euros have failure")
    public void theCustomerGoesToTheCashierToPayAnAmountOfEurosHaveFailure(double arg0) {
        Card card = cardHandler.getCardById(cardId);
        purchase = new Purchase((float) arg0, LocalDateTime.now(),society,card);
        Assertions.assertThrows(NotEnoughBalanceException.class, () -> paymentService.createPurchasePayWithCard(purchase));
    }

    @Then("the customer's balance is still at {double} euros")
    public void theCustomerSBalanceIsStillAtEuros(double arg0) throws ResourceNotFoundException {
        Card card1 = cardHandler.getCardById(cardId);
        assertEquals(arg0, card1.getBalance());
    }
}
