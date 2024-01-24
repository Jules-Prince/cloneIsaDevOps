package fr.univcotedazur.multifidelity.cucumber.advantage;

import fr.univcotedazur.multifidelity.components.Catalog;
import fr.univcotedazur.multifidelity.components.GiftService;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.Validity;
import fr.univcotedazur.multifidelity.exceptions.NotAvailableAdvantageException;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import fr.univcotedazur.multifidelity.repositories.SelectedAdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import fr.univcotedazur.multifidelity.repositories.ValidityRepository;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UseAdvantageWithDuration {

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
    private ValidityRepository validityRepository;

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

    Validity validity;


    @After
    public void cleanUpContext() throws Exception {
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        validityRepository.deleteAll();
    }

    @Given("a society {string}")
    public void aSociety(String arg0) {
        partner = partnerRepository.save(new Partner());
        society = new Society(arg0, partner, LocalTime.NOON, LocalTime.MIDNIGHT, "8 rue des fleurs");
        society = societyRepository.save(society);
    }

    @And("an advantage {string} from the society {string}")
    public void anAdvantageFromTheSociety(String arg0, String arg1) {
        advantage = new Advantage();
        advantage.setName(arg0);
        advantage.setSociety(society);
        validity = new Validity();
        validity.setDuration(Duration.ofDays(7));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(1);
        validity.setId(1L);
        validity = validityRepository.save(validity);
        advantage.setValidity(validity);
        advantage = advantageRepository.save(advantage);
    }

    @And("a customer {string} who has selected the advantage {string} from the society {string}")
    public void aCustomerWhoHasSelectedTheAdvantageFromTheSociety(String arg0, String arg1, String arg2) {
        card = cardRepository.save(new Card());
        consumer = new Consumer();
        consumer.setCard(card);
        consumer = consumerRepository.save(consumer);
        selectedAdvantage = advantageSelecter.selectAdvantage(advantage, consumer);
    }

    @Given("the customer never used his advantage")
    public void theCustomerNeverUsedHisAdvantage() {
        selectedAdvantage.setNumberOfUse(0);
        selectedAdvantage.setLastUse(null);
    }

    @When("the customer use his advantage {int} time")
    public void theCustomerUseHisAdvantageTime(int arg0) {
        for (int i = 0; i < arg0; i++) {
            advantageSelecter.useAdvantage(selectedAdvantage);
        }
    }

    @Then("the customer has used his advantage {int} time")
    public void theCustomerHasUsedHisAdvantageTime(int arg0) {
        assert selectedAdvantage.getNumberOfUse() == arg0;
    }

    @Then("the customer cannot use his advantage")
    public void theCustomerCannotUseHisAdvantage() {
        assertThrows(NotAvailableAdvantageException.class, () -> advantageSelecter.useAdvantage(selectedAdvantage));
    }

    @Given("the advantage is expired")
    public void theAdvantageIsExpired() {
        selectedAdvantage.getAdvantage().getValidity().setDuration(Duration.ofDays(0));
    }

    @Then("the customer cannot use his advantage anymore today")
    public void theCustomerCannotUseHisAdvantageAnymoreToday() {
        assertThrows(NotAvailableAdvantageException.class, () -> advantageSelecter.useAdvantage(selectedAdvantage));
    }

    @Given("the advantage is now illimited")
    public void theAdvantageIsNowIllimited() {
        selectedAdvantage.getAdvantage().getValidity().setNumberOfUse(0);
    }
}
