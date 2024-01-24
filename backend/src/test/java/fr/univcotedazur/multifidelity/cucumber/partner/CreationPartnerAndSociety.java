package fr.univcotedazur.multifidelity.cucumber.partner;

import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistingAccountException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.PartnerAccountManager;
import fr.univcotedazur.multifidelity.interfaces.PartnerGetter;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.interfaces.SocietyModifier;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@SpringBootTest
@Transactional
public class CreationPartnerAndSociety {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private PartnerAccountManager partnerAccountManager;

    @Autowired
    private SocietyModifier societyModifier;

    @Autowired
    private PartnerGetter partnerGetter;

    @Autowired
    private SocietyGetter societyGetter;

    Partner partner;
    Society society;

    Partner partner2;

    @Before
    public void beforeContext() throws Exception{
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
    }


    @When("the mayor creates a new partner {string} with email {string}")
    public void theMayorCreatesANewPartner(String arg0, String email) throws AlreadyExistingAccountException, AlreadyExistSimilarRessourceException {
        partner = new Partner( arg0, email, "mdp");
        partnerAccountManager.addPartnerAccount(partner);
    }

    @Then("the partner {string} is present in the repository")
    public void thePartnerIsPresentInTheRepository(String arg0) throws ResourceNotFoundException {
        Assertions.assertEquals(1, partnerRepository.count());
        Partner p = partnerGetter.getPartnerById(partner.getId());
        Assertions.assertEquals(p, partner);
    }

    @Given("a partner {string}")
    public void aPartner(String arg0) {
        partner = new Partner( arg0, "s@gmail.com", "mdp");
        partnerRepository.save(partner);
    }

    @When("the mayor creates a new society called {string} for the partner {string}")
    public void theMayorCreatesANewSocietyCalledForThePartner(String arg0, String arg1) throws AlreadyExistSimilarRessourceException {
        partner2 = new Partner( arg1, "s2@gmail.com", "mdp");
        partner2 =partnerRepository.save(partner2);
        society = new Society(arg0, partner2, LocalTime.of(8, 0), LocalTime.of(18, 0), "1 rue Gambetta");
        societyModifier.addSociety(society);
    }

    @Then("the society is present in the repository")
    public void theSocietyIsPresentInTheRepository() throws ResourceNotFoundException {
        Assertions.assertEquals(1, societyRepository.count());
        Society s = societyGetter.getSocietyById(society.getId());
        Assertions.assertEquals(s, society);
    }

    @Then("the society is linked to the partner")
    public void theSocietyIsLinkedToThePartner() {
        Assertions.assertEquals(society.getPartner(), partner2);
    }


    @Transactional(propagation=Propagation.NEVER)
    @Then("the mayor doesnt creates the new partner {string} with email {string}")
    public void theMayorDoesntCreatesTheNewPartner(String arg0, String email) throws AlreadyExistingAccountException, ResourceNotFoundException {
        Partner copieur = new Partner( arg0, email, "mdp2");
        Assertions.assertThrows(AlreadyExistingAccountException.class, () -> partnerAccountManager.addPartnerAccount(copieur));
    }

    @Then("the partner {string} is not present in the repository")
    public void thePartnerIsNotPresentInTheRepository(String arg0) {
        Assertions.assertEquals(1, partnerRepository.count());
    }

    @Transactional(propagation= Propagation.NEVER)
    @Then("the mayor creates a new partner {string} with email {string} and thie is failure")
    public void theMayorCreatesANewPartnerWithEmailAndThieIsFailure(String arg0, String arg1) {
        partner = new Partner(arg0, arg1, "mdp");
        Assertions.assertThrows(AlreadyExistSimilarRessourceException.class, () ->  partnerAccountManager.addPartnerAccount(partner));

    }

    @Transactional(propagation=Propagation.NEVER)
    @Then("the mayor creates a new society called {string} for the partner {string} there is failure")
    public void theMayorCreatesANewSocietyCalledForThePartnerThereIsFailure(String arg0, String arg1) {
        /*
        society = new Society(arg0, partner2, LocalTime.of(8, 0), LocalTime.of(18, 0), "1 rue Gambetta");
        Assertions.assertThrows(AlreadyExistSimilarRessourceException.class, () -> societyModifier.addSociety(society));

         */
    }
}