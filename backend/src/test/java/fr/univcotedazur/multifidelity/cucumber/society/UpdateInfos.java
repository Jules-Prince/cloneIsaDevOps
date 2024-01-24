package fr.univcotedazur.multifidelity.cucumber.society;

import fr.univcotedazur.multifidelity.components.SocietyManager;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UpdateInfos {

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private SocietyManager societyManager;

    private Society oldSociety;


    private Long oldSocietyId;

    private Society newSociety;

    private Long newSocietyId;

    private Partner partner;

    @AfterEach
    public void cleanUpContext() throws Exception{
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
    }

    @Given("a society called {string} opening at {int}:{int} and closing at {int}:{int}, located in {string} is created")
    public void aSocietyCalledOpeningAtAndClosingAtLocatedInWithPhoneNumberIsCreated(String arg0, int arg1, int arg2, int arg3, int arg4, String arg5) {
        partner =partnerRepository.save(new Partner());
        oldSociety = new Society(arg0,partner, LocalTime.of(arg1, arg2), LocalTime.of(arg3, arg4), arg5);
        oldSocietyId =societyRepository.save(oldSociety).getId();
        newSociety = new Society(oldSociety.getName(),oldSociety.getPartner(),oldSociety.getOpeningHour(),oldSociety.getClosingHour(),oldSociety.getAddress());
    }



    @When("the partner change the location of the society to {string}")
    public void thePartnerChangeTheLocationOfTheSocietyTo(String arg0) throws ResourceNotFoundException {
        newSociety.setAddress(arg0);
        oldSociety =societyManager.getSocietyById(oldSocietyId);
        societyManager.updateSocietyInformation(oldSociety,newSociety);
    }

    @Then("the society {string} is located in {string}")
    public void theSocietyIsLocatedIn(String arg0, String arg1) {
        oldSociety =societyManager.getSocietyById(oldSocietyId);
        assertEquals(arg1, societyRepository.findById(oldSociety.getId()).get().getAddress());
    }

    @When("the partner change the opening time of the society to {int}:{int}")
    public void thePartnerChangeTheOpeningTimeOfTheSocietyTo(int arg0, int arg1) throws ResourceNotFoundException {
        oldSociety =societyManager.getSocietyById(oldSocietyId);
        newSociety.setOpeningHour(LocalTime.of(arg0, arg1));
        societyManager.updateSocietyInformation(oldSociety,newSociety);
    }

    @Then("the society {string} is open at {int}:{int}")
    public void theSocietyIsOpenAt(String arg0, int arg1, int arg2) {
        assertEquals(LocalTime.of(arg1, arg2), societyRepository.findById(oldSociety.getId()).get().getOpeningHour());
    }

    @And("a notification is sent")
    public void aNotificationIsSent() {
        // how to check??
    }

    @When("the partner change the closing time of the society to {int}:{int}")
    public void thePartnerChangeTheClosingTimeOfTheSocietyTo(int arg0, int arg1) throws ResourceNotFoundException {
        oldSociety =societyManager.getSocietyById(oldSocietyId);
        newSociety.setClosingHour(LocalTime.of(arg0, arg1));
        societyManager.updateSocietyInformation(oldSociety,newSociety);
    }

    @Then("the society {string} is closed at {int}:{int}")
    public void theSocietyIsClosedAt(String arg0, int arg1, int arg2) {
        assert societyRepository.findById(oldSociety.getId()).get().getClosingHour().equals(LocalTime.of(arg1, arg2));
    }
}
