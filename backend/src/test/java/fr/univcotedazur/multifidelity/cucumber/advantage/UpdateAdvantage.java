package fr.univcotedazur.multifidelity.cucumber.advantage;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.interfaces.CatalogModifier;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@SpringBootTest
@Transactional
public class UpdateAdvantage {
    @Autowired
    AdvantageRepository advantageRepository;


    @Autowired
    SocietyRepository societyRepository;


    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    CatalogModifier advantageModifier;

    Society society;
    Partner partner;
    Advantage advantage;
    Long id;

    @Before
    public void cleanUpContext() throws Exception{
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
    }
    @Given("a society called {string}")
    public void aSocietyCalled(String arg0) {
        partner = new Partner("Jean", "jean@gmail.com", "1234");
        partner =partnerRepository.save(partner);
        society = new Society(arg0, partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "15 rue b");
        society =societyRepository.save(society);
    }

    @And("the society {string} propose an advantage {string} with initial price {int}€, price {int}€, point {int}, that is not a vfp advantage, start validity {int}-{int}-{int} at {int}h{int} and end validity {int}-{int}-{int} at {int}h{int}")
    public void theSocietyProposeAnAdvantage(String arg0, String arg1, int arg2, int arg3, int arg4, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13, int arg14, int arg15) {
        advantage = new Advantage(arg1, arg4, false, arg3, society, arg2);
        advantageModifier.createAdvantage(advantage);
        id = advantage.getId();
    }

    @When("the society update the advantage {string} with inital price {int}€, with price {int}€, point {int}, that is a vfp advantage, start validity {int}-{int}-{int} at {int}h{int} and end validity {int}-{int}-{int} at {int}h{int}")
    public void theSocietyUpdateTheAdvantage(String arg0, int arg1, int arg2, int arg3, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13, int arg14) {
        Advantage newAdvantage = new Advantage(arg0, arg3, true, arg1, society, arg2);
        advantageModifier.updateAdvantage(advantage, newAdvantage);
    }

    @Then("the advantage {string} should have inital price {int}€, price {int}€, point {int}, that is a vfp advantage, start validity {int}-{int}-{int} at {int}h{int} and end validity {int}-{int}-{int} at {int}h{int}")
    public void theAdvantageShouldHave(String arg0, int arg1, int arg2, int arg3, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13, int arg14) {
        Assertions.assertEquals(arg0, advantage.getName());
        Assertions.assertEquals(arg1, advantage.getInitialPrice());
        Assertions.assertEquals(arg2, advantage.getPrice());
        Assertions.assertEquals(arg3, advantage.getPoint());
        Assertions.assertTrue(advantage.isVfpAdvantage());
        Assertions.assertEquals(society, advantage.getSociety());
        Assertions.assertEquals(id, advantage.getId());
    }
}
