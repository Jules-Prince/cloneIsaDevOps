package fr.univcotedazur.multifidelity.cucumber.advantage;

import fr.univcotedazur.multifidelity.components.Catalog;
import fr.univcotedazur.multifidelity.components.GiftService;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;
import fr.univcotedazur.multifidelity.exceptions.NotAvailableAdvantageException;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import fr.univcotedazur.multifidelity.repositories.SelectedAdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@SpringBootTest
@Transactional
public class RetrieveSelectedAdvantage {


    @Autowired
    private SelectedAdvantageRepository selectedAdvantageRepository;


    @Autowired
    private SocietyRepository societyRepository;


    @Autowired
    private AdvantageRepository advantageRepository;


    @Autowired
    private PartnerRepository partnerRepository;


    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    Catalog catalog;

    @Autowired
    GiftService advantageSelecter;



    Society society;
    Advantage advantage;

    SelectedAdvantage selectedAdvantage;

    Partner partner;

    Card card;

    Consumer consumer;

    Long selectedAdvantageId;


    @Before
    public void cleanUpContext() throws Exception{
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
    }


    @Given("a society name {string}")
    public void a_society_name(String string) {
        partner = partnerRepository.save(new Partner());
        society = new Society(string, partner, LocalTime.NOON, LocalTime.MIDNIGHT, "8 rue des fleurs");
        society =societyRepository.save(society);
        card = cardRepository.save(new Card());
        consumer = new Consumer();
        consumer.setCard(card);
        consumer =consumerRepository.save(consumer);
    }
    @Given("a Advantage avaible at the same shop")
    public void a_advantage_avaible_at_the_same_shop() {
        advantage = new Advantage();
        advantage.setSociety(society);
        advantage = advantageRepository.save(advantage);
    }
    @Given("a customer who is choosing an advantage from the shop")
    public void a_customer_who_is_choosing_an_advantage_from_the_shop() {
        selectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, advantage, null);
        selectedAdvantageId = selectedAdvantageRepository.save(selectedAdvantage).getId();
    }
    @Given("a customer who retrieved his advantage")
    public void a_customer_who_retrieved_his_advantage() throws NotAvailableAdvantageException {
        selectedAdvantage = selectedAdvantageRepository.findById(selectedAdvantageId).get();
        advantageSelecter.useAdvantage(selectedAdvantage);
    }
    @Then("the advantage is set to Retrived status")
    public void the_advantage_is_set_to_retrived_status() {
        selectedAdvantage = selectedAdvantageRepository.findById(selectedAdvantageId).get();
        Assertions.assertEquals(StateSelectedAdvantage.NOT_AVAILABLE, selectedAdvantage.getState());
    }

    @Given("a customer who retrieved his advantage one time")
    public void aCustomerWhoRetrievedHisAdvantageOneTime() throws NotAvailableAdvantageException {
        selectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, advantage, null);
        selectedAdvantageId = selectedAdvantageRepository.save(selectedAdvantage).getId();
        selectedAdvantage = selectedAdvantageRepository.findById(selectedAdvantageId).get();
        advantageSelecter.useAdvantage(selectedAdvantage);
    }

    @Transactional(propagation= Propagation.NEVER)
    @Then("a customer who retrieved his advantage two time and there is failure")
    public void aCustomerWhoRetrievedHisAdvantageTwoTimeAndThereIsFailure() {
        selectedAdvantage = selectedAdvantageRepository.findById(selectedAdvantageId).get();
        Assertions.assertThrows(NotAvailableAdvantageException.class, () -> advantageSelecter.useAdvantage(selectedAdvantage));
    }
}
