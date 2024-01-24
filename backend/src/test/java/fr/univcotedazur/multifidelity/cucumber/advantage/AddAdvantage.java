package fr.univcotedazur.multifidelity.cucumber.advantage;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.interfaces.CatalogModifier;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class AddAdvantage {

    @Autowired
    private AdvantageRepository advantageRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private CatalogModifier advantageModifier;


    private Society society;

    @Before
    public void cleanUpContext() throws Exception{
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @Given("a society")
    public void aSociety() {
        society = new Society();
        society = societyRepository.save(society);
    }

    @When("the partner create a  advantage")
    public void thePartnerCreateAAdvantage()  {
        Advantage advantage = new Advantage("café offert",12,false, 2.5F,society,0);
        advantageModifier.createAdvantage(advantage);
    }

    @Then("the advantage is present in the repository")
    public void theAdvantageIsPresentInTheRepository() {
        assertEquals(1,  advantageRepository.count());
    }

}
