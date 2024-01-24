package fr.univcotedazur.multifidelity.cucumber.showCase;

import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.interfaces.PartnerGetter;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class ShowPartners {

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    PartnerGetter partnerGetter;

    public void beforeContext() throws Exception{
        partnerRepository.deleteAll();
    }

    @AfterEach
    public void cleanUpContext() throws Exception{
        partnerRepository.deleteAll();
    }
    @Given("a partner named {string}")
    public void aPartnerNamed(String arg0) {
        Partner partner = new Partner();
        partner.setName(arg0);
        partnerRepository.save(partner);
    }

    @When("I go to the partner list")
    public void iGoToThePartnerList() {
        partnerGetter.getAllPartners();
    }

    @Then("I see {string}")
    public void iSee(String arg0) {
        assertTrue(partnerGetter.getAllPartners().contains(partnerGetter.getPartnerByName(arg0)));
    }

    @Then("there is {int} partner")
    public void thereIsPartner(int arg0) {
        assertEquals(arg0, partnerGetter.getAllPartners().size());
    }
}
