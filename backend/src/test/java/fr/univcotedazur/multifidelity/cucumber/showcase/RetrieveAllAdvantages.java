package fr.univcotedazur.multifidelity.cucumber.showCase;

import fr.univcotedazur.multifidelity.components.Catalog;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
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
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class RetrieveAllAdvantages {

    @Autowired
    AdvantageRepository advantageRepository;

    @Autowired
    SocietyRepository societyRepository;

    @Autowired
    ConsumerRepository consumerRepository;

    @Autowired
    Catalog advantageHandler;

    @Autowired
    SocietyGetter societyGetter;

    Consumer consumer;

    @AfterEach
    public void cleanUpContext() throws Exception{
        advantageRepository.deleteAll();
        consumerRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @Before
    public void beforeContext() throws Exception{
        advantageRepository.deleteAll();
        consumerRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @Given("a consumer")
    public void a_consumer() {
        this.consumer = new Consumer();
        this.consumer = consumerRepository.save(consumer);
    }

    @Given("a advantage repository with {int} advantages")
    public void a_advantage_repository_with_advantages(Integer nbAdvantage) {
        IntStream.range(0, nbAdvantage).forEach(i -> advantageRepository.save(new Advantage()));
    }

    @When("the customer retrieve all advantages and the customer has {int} advantages")
    public void the_customer_retrieve_all_advantages(Integer nbAdvantage) {
        List<Advantage> allAdvantage = advantageHandler.getAllAdvantages();
        assertEquals(nbAdvantage, allAdvantage.size());
    }

    @Given("an advantage {string} by his society named {string}")
    public void anAdvantageByHisSocietyNamed(String arg0, String arg1) {
        Advantage advantage = new Advantage();
        advantage.setName(arg0);
        Society society = new Society();
        society.setName(arg1);
        advantage.setSociety(society);
        societyRepository.save(society);
        advantageRepository.save(advantage);
    }

    @When("the client go to the society list and filter by society {string}")
    public void theClientGoToTheSocietyListAndFilterBySociety(String arg0) {
        advantageHandler.getAllAdvantagesBySociety(societyGetter.getSocietyByName(arg0));
    }

    @Then("the client see only {string}")
    public void theClientSeeOnly(String arg0) {
        assertEquals(arg0, advantageHandler.getAllAdvantagesBySociety(societyGetter.getSocietyByName("Boulangerie")).get(0).getName());
    }

    @Then("there is only {int} advantage")
    public void thereIsOnlySociety(int arg0) {
        assertEquals(arg0, advantageHandler.getAllAdvantagesBySociety(societyGetter.getSocietyByName("Boulangerie")).size());
    }

    @Given("a society named {string} giving {int} advantage")
    public void aSocietyNamedGivingAdvantage(String arg0, int arg1) {
        Society society = new Society();
        society.setName(arg0);
        IntStream.range(0, arg1).forEach(i -> {
            Advantage advantage = new Advantage();
            advantage.setName("advantage" + i);
            advantage.setSociety(society);
            advantageRepository.save(advantage);
        });
        societyRepository.save(society);
    }
}
