package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.interfaces.SocietyModifier;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Commit
public class SocietyManagerTest {

    @Autowired
    SocietyRepository societyRepository;


    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    private SocietyGetter societyGetter;

    @Autowired
    private SocietyModifier societyModifier;

    @Autowired
    SocietyManager societyManager;

    Society society;

    Partner partner;

    @BeforeEach
    public void setUpContext() {
        partner = partnerRepository.save(new Partner());
        society = new Society();
        society.setPartner(partner);
        society.setName("Boulangerie");
        society =societyRepository.save(society);
    }

    @AfterEach
    public void cleanUpContext() throws Exception{
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
    }

    @Test
    public void getAllSocietiesTest() throws Exception {
        assertEquals(1, societyGetter.getAllSocieties().size());
    }

    @Test
    public void getSocietyByNameTest() throws Exception {
        assertEquals(society, societyGetter.getSocietyByName("Boulangerie"));
    }

    @Test
    public void getSocietyByIdTest() throws Exception {
        assertEquals(society, societyGetter.getSocietyById(society.getId()));
    }

    @Test
    public void addSocietyTest() throws AlreadyExistSimilarRessourceException {
        Society society2 = new Society("Boulangerie2", partner, LocalTime.of(8,0), LocalTime.of(19,0), "73 rue blabla");
        Society addedSociety = societyModifier.addSociety(society2);
        assertEquals(addedSociety, society2);
        assertEquals(2, societyGetter.getAllSocieties().size());
    }

    @Test
    public void updateSocietyTest() {
        Society newSociety = new Society( "Boulangerie2", new Partner(), LocalTime.of(9,0), LocalTime.of(18,0), "73 rue blibli");
        Society societyUpdated = societyModifier.updateSocietyInformation(society, newSociety);
        assertEquals(societyUpdated.getId(),society.getId());
        assertEquals(societyUpdated.getPartner(),society.getPartner());
        assertEquals("Boulangerie", societyUpdated.getName());
        assertEquals(societyUpdated.getOpeningHour(), LocalTime.of(9, 0));
        assertEquals(societyUpdated.getClosingHour(), LocalTime.of(18, 0));
        assertEquals("73 rue blibli", societyUpdated.getAddress());
    }


}