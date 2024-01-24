package fr.univcotedazur.multifidelity.cucumber.showCase;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import fr.univcotedazur.multifidelity.interfaces.ConsumerModifier;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
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
public class ShowSocieties {

    @Autowired
    ConsumerRepository consumerRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    SocietyRepository societyRepository;

    @Autowired
    SocietyGetter societyGetter;

    @Autowired
    ConsumerGetter consumerGetter;

    @Autowired
    ConsumerModifier consumerModifier;
    List<Society> societyList;

    Consumer consumer;


    Long consumerId;

    Society society;


    Long societyId;



    @Before
    public void beforeContext() throws Exception{
        consumerRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
    }


    @Given("a client logged in as {string} with password {string}")
    public void aClientLoggedInAsWithPassword(String arg0, String arg1) {
         consumer = new Consumer(arg0, arg1, "123456789", "AB-123-CD",null);
        consumerId =consumerRepository.save(consumer).getId();
    }

    @Given("a partner and his society named {string}")
    public void aPartnerAndHisSocietyNamed(String arg0) {
        Partner partner = new Partner();
        partnerRepository.save(partner);
         society = new Society();
        society.setPartner(partner);
        society.setName(arg0);
        societyId =societyRepository.save(society).getId();
    }

    @When("the client go to the society list")
    public void theClientGoToTheSocietyList() {
        societyList = societyGetter.getAllSocieties();
    }

    @Then("the client see {string}")
    public void theClientSee(String arg0) {
        Assertions.assertTrue(societyList.stream().map(Society::getId).toList().contains(society.getId()));
        Assertions.assertEquals(society.getName(), arg0);
    }

    @Then("there is {int} society")
    public void thereIsSociety(int arg0) {
        Assertions.assertEquals(societyList.size(), arg0);
        Assertions.assertEquals(societyRepository.count(), arg0);
    }


    @When("the client add {string} to his fav list")
    public void theClientAddToHisFavList(String arg0) {
        consumer = consumerGetter.getConsumerById(consumerId);
        society = societyGetter.getSocietyById(societyId);
        consumerModifier.addFavSociety(society, consumer);
    }

    @When("the client go to the fav society list")
    public void theClientGoToTheFavSocietyList() {
        consumer = consumerGetter.getConsumerById(consumerId);
        societyList = consumerGetter.getFavSocietiesByConsumer(consumer);
    }


    @Then("the client see {string} in the fav list")
    public void theClientSeeInTheFavList(String arg0) {
        consumer = consumerGetter.getConsumerById(consumerId);
        societyList = consumerGetter.getFavSocietiesByConsumer(consumer);
        Assertions.assertTrue(societyList.stream().map(Society::getId).toList().contains(society.getId()));
    }

    @Then("there is {int} fav society")
    public void thereIsFavSociety(int arg0) {
        consumer = consumerGetter.getConsumerById(consumerId);
        societyList = consumerGetter.getFavSocietiesByConsumer(consumer);
        Assertions.assertEquals(societyList.size(), arg0);
    }
}