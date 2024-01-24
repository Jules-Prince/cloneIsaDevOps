package fr.univcotedazur.multifidelity.cucumber.advantage;

import fr.univcotedazur.multifidelity.entities.*;
import fr.univcotedazur.multifidelity.interfaces.AdvantageFinder;
import fr.univcotedazur.multifidelity.interfaces.AdvantageGetter;
import fr.univcotedazur.multifidelity.interfaces.ConsumerFinder;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class EligibleAdvantage {

    @Autowired
    ConsumerRepository consumerRepository;

    @Autowired
    SocietyRepository societyRepository;

    @Autowired
    AdvantageRepository advantageRepository;

    @Autowired
    AdvantageGetter advantageGetter;

    @Autowired
    ConsumerFinder consumerFinder;

    @Autowired
    AdvantageFinder advantageFinder;

    @Autowired
    CardRepository cardRepository;


    Consumer rich;
    Consumer poor;
    Society forTheRich;
    Society forThePoor;
    Advantage forTheRichOnly;
    Advantage forEveryone;
    List<Advantage> list;

    @Before
    public void cleanUpContext() throws Exception{
        advantageRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @And("a consumer with email {string} not vfp with {int} points on his card")
    public void aConsumerWithEmailNotVfp(String arg0, int points) {
        poor = new Consumer();
        poor.setEmail(arg0);
        poor.setVfp(false);
        Card card = new Card();
        card.setPoint(points);
        card =cardRepository.save(card);
        poor.setCard(card);
        poor =consumerRepository.save(poor);
    }

    @Given("a society {string} that propose an advantage {string} vfp-only that cost {int} points")
    public void aSocietyThatProposeAnAdvantageVfpOnly(String arg0, String arg1, int points) {
        forTheRich = new Society();
        forTheRich.setName(arg0);
        forTheRich =societyRepository.save(forTheRich);
        forTheRichOnly = new Advantage();
        forTheRichOnly.setName(arg1);
        forTheRichOnly.setVfpAdvantage(true);
        forTheRichOnly.setSociety(forTheRich);
        forTheRichOnly.setPoint(points);
        forTheRichOnly = advantageRepository.save(forTheRichOnly);
    }

    @And("a consumer with email {string} vfp with {int} points on his card")
    public void aConsumerWithEmailVfp(String arg0, int points) {
        rich = new Consumer();
        rich.setEmail(arg0);
        rich.setVfp(true);
        Card card = new Card();
        card.setPoint(points);
        card = cardRepository.save(card);
        rich.setCard(card);
        consumerRepository.save(rich);
    }

    @When("the consumer with email {string} wants to see the advantages")
    public void theConsumerWithEmailWantsToSeeTheAdvantages(String arg0) {
        Optional<Consumer> consumer = consumerFinder.findConsumerByEmail(arg0);
        list = advantageGetter.getAllAdvantageEligibleByConsumer(consumer.get());
    }

    @Then("the consumer should see the advantage {string}")
    public void theConsumerShouldSeeTheAdvantage(String arg0) {
        assertTrue(list.stream().anyMatch(advantage -> advantage.getName().equals(arg0)));
    }

    @Then("the consumer should not see the advantage {string}")
    public void theConsumerShouldNotSeeTheAdvantage(String arg0) {
        assertTrue(list.stream().noneMatch(advantage -> advantage.getName().equals(arg0)));
    }

    @And("a society {string} that propose an advantage {string} not vfp-only that cost {int} points")
    public void aSocietyThatProposeAnAdvantageNotVfpOnly(String arg0, String arg1, int points) {
        forThePoor = new Society();
        forThePoor.setName(arg0);
        forThePoor =societyRepository.save(forThePoor);
        forEveryone = new Advantage();
        forEveryone.setName(arg1);
        forEveryone.setVfpAdvantage(false);
        forEveryone.setSociety(forThePoor);
        forEveryone.setPoint(points);
        forEveryone = advantageRepository.save(forEveryone);
    }

    @When("the advantage {string} is updated to cost {int} points")
    public void theAdvantageIsUpdatedToCostPoints(String arg0, int arg1) {
        forTheRichOnly.setPoint(arg1);
        advantageRepository.save(forTheRichOnly);
    }
}
