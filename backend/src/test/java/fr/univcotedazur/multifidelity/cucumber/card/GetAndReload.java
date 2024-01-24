package fr.univcotedazur.multifidelity.cucumber.card;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.interfaces.Bank;
import fr.univcotedazur.multifidelity.interfaces.CardClientService;
import fr.univcotedazur.multifidelity.interfaces.CardGetter;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GetAndReload {

    @Autowired
    ConsumerRepository consumerRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardGetter cardGetter;

    @Autowired
    ConsumerGetter consumerGetter;

    @Autowired
    CardClientService cardClientService;

    @Autowired
    private Bank bank;

    @Before
    public void setUp() {
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        when(bank.pay(any(Consumer.class), anyFloat())).thenReturn(true);
    }

    Consumer consumer;


    @Given("a consumer logged in with email {string} and password {string} and credit card {string} and balance {int}")
    public void aConsumerLoggedInWithEmailAndPasswordAndCreditCardAndBalance(String mail, String password, String card, int amount) {
        consumer = new Consumer(mail, password, card, "12-AZE-34",null);
        consumerRepository.save(consumer);
        cardClientService.createCard(consumer);
        cardClientService.reloadBalance(amount, consumer);
    }

    @When("the consumer wants to see his balance")
    public void theConsumerWantsToSeeHisBalance() {
        cardClientService.getBalance(consumer);
    }

    @Then("the consumer should see his balance at {int}")
    public void theConsumerShouldSeeHisBalanceAt(int amount) {
        assertEquals(amount, cardClientService.getBalance(consumer));
    }


    @When("the consumer wants to reload his balance with {int}")
    public void theConsumerWantsToReloadHisBalanceWith(int amount) {
        try {
            cardClientService.reloadBalance(amount, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}