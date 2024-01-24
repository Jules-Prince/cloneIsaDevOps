package fr.univcotedazur.multifidelity.cucumber.card;

import fr.univcotedazur.multifidelity.components.CardHandler;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RetryCard {

    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private ConsumerRepository consumerRepository;


    @Autowired
    private CardHandler cardHandler;

    private Consumer consumer;

    @AfterEach
    public void cleanUpContext() throws Exception{
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @Before
    public void beforeContext() throws Exception{
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @Given("a new consumer")
    public void aNewConsumer() {
        consumer = new Consumer();
        consumer =consumerRepository.save(consumer);
    }

    @When("the consumer retry a  card")
    public void theConsumerRetryACard() throws AlreadyExistSimilarRessourceException {
        cardHandler.createCard(consumer);
    }

    @Then("the card is present in the repository")
    public void theCardIsPresentInTheRepository() {
        assertEquals(1, StreamSupport.stream(cardRepository.findAll().spliterator(), false).toList().size());
    }

    @Then("the consumer retry new a  card have failure")
    public void theConsumerRetryNewACardHaveFailure() {
        Assertions.assertThrows(AlreadyExistSimilarRessourceException.class, () ->  cardHandler.createCard(consumer));
    }
}
