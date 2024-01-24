package fr.univcotedazur.multifidelity.cucumber.advantage;

import fr.univcotedazur.multifidelity.components.Catalog;
import fr.univcotedazur.multifidelity.components.GiftService;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughPointException;
import fr.univcotedazur.multifidelity.interfaces.SelectedAdvantageGetter;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.SelectedAdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SelectAdvantage {

    @Autowired
    private AdvantageRepository advantageRepository;


    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private SelectedAdvantageRepository selectedAdvantageRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private SelectedAdvantageGetter advantageAvailability;


    @Autowired
    private Catalog catalog;

    @Autowired
    GiftService advantageSelecter;



    private Society society;

    private Card card;


    private Advantage advantage;

    private Consumer consumer;

    private Long consumerId;

    @Before
    public void cleanUpContext() throws Exception{
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
    }



    @Given("a society with an advantage at {int} points, a consumer with a card at {int} points")
    public void aSocietyWithAnAdvantageAtPointsAConsumerWithACardAtPoints(int arg0, int arg1) {
        society = new Society();
        society =societyRepository.save(society);
        card = new Card(arg1,20);
        card = cardRepository.save(card);
        consumer = new Consumer("email","paswd","creditcard","licence plate",null);
        consumer.setCard(card);
        consumerId =consumerRepository.save(consumer).getId();
        advantage = new Advantage("café offert",arg0,false, 2.5F,society,0);
        advantage =advantageRepository.save(advantage);
    }

    @When("the consumer select a  advantage")
    public void theConsumerSelectAAdvantage() throws NotEnoughPointException {
        consumer = consumerRepository.findById(consumerId).get();
        advantageSelecter.selectAdvantage(advantage,consumer);
        consumerRepository.save(consumer);
    }

    @Then("the selected advantage is present in the repository")
    public void theSelectedAdvantageIsPresentInTheRepository() {
        assertEquals(1,  selectedAdvantageRepository.count());

    }

    @Then("the selected advantage have state available")
    public void theSelectedAdvantageHaveStateAvailable() {
        assertEquals(StateSelectedAdvantage.AVAILABLE,  advantageAvailability.getAllSelectedAdvantages().stream().findFirst().get().getState());
    }

    @Then("the card of consumer have {int} points")
    public void theCardOfConsumerHavePoints(int arg0) {
        consumer = consumerRepository.findById(consumerId).get();
        assertEquals(arg0,  consumer.getCard().getPoint());
    }


    @When("a advantage at {int} points")
    public void aAdvantageAtPoints(int arg0) {
        advantage.setPoint(arg0);
        advantageRepository.save(advantage);
    }

    @Then("the consumer select the advantage with failure")
    public void theConsumerSelectTheAdvantageWithFailure() {
        consumer = consumerRepository.findById(consumerId).get();
        Assertions.assertThrows(NotEnoughPointException.class, () ->   advantageSelecter.selectAdvantage(advantage,consumer));
    }
}
