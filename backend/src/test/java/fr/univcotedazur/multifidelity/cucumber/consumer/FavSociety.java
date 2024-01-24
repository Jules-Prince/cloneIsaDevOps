package fr.univcotedazur.multifidelity.cucumber.consumer;

import fr.univcotedazur.multifidelity.components.ConsumerHandler;
import fr.univcotedazur.multifidelity.components.ConsumerManager;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class FavSociety {

    @Autowired
    ConsumerHandler consumerHandler;

    @Autowired
    ConsumerManager consumerManager;

    @Autowired
    ConsumerRepository consumerRepository;

    @Autowired
    SocietyRepository societyRepository;

    Consumer consumer;
    Society society;

    @Before
    public void cleanUpContext() throws Exception{
        consumerRepository.deleteAll();
        societyRepository.deleteAll();
    }


    @Given("a consumer and a society")
    public void consumer_with_email_and_password(){
        consumer = new Consumer();
        consumer = consumerRepository.save(consumer);
        society = new Society();
        society =societyRepository.save(society);
    }

    @When("the consumer add the society to his favorite society")
    public void theConsumerAddTheSocietyToHisFavoriteSociety() {
        consumerHandler.addFavSociety(society, consumer);
    }

    @Then("the consumer has {int} favorite society")
    public void theConsumerHasFavoriteSociety(int arg0) throws ResourceNotFoundException {
        int check = consumerHandler.getFavSocietiesByConsumer(consumer).size();
        Assertions.assertEquals(arg0, check);
    }

    @Then("the consumer favorite society is the society given")
    public void theConsumerFavoriteSocietyIsTheSocietyGiven() throws ResourceNotFoundException {
        List<Society> check = consumerHandler.getFavSocietiesByConsumer(consumer);
        Assertions.assertEquals(check.size(), 1);
    }

    @When("the consumer delete the society from his favorite society")
    public void theConsumerDeleteTheSocietyFromHisFavoriteSociety() {
        consumerHandler.deleteFavSociety(society, consumer);
    }
}
