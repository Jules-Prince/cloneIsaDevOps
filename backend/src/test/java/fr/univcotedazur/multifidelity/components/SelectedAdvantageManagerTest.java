package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.SelectedAdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Commit
public class SelectedAdvantageManagerTest {

    @Autowired
    private SelectedAdvantageRepository selectedAdvantageRepository;

    @Autowired
    private AdvantageRepository advantageRepository;

    @Autowired
    private SocietyRepository societyRepository;


    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private AdvantageManager advantageManager;



    @Autowired
    private SelectedAdvantageManager selectedAdvantageManager;


    private Advantage advantage;


    private Society society;

    private Card card;


    @BeforeEach
    public void setUpContext() {
        society = new Society();
        society =societyRepository.save(society);
        card = new Card();
        card = cardRepository.save(card);
        advantage = new Advantage("café offert",12,false, 2.5F,society,0);
    }

    @AfterEach
    public void cleanUpContext() throws Exception{
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
        cardRepository.deleteAll();
    }


    @Test
    public void addSelectAdvantageTest()  {
        advantageManager.addAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = selectedAdvantageManager.addSelectAdvantage(advantage,card);
        assertEquals(1,  selectedAdvantageRepository.count());
        assertEquals(StateSelectedAdvantage.AVAILABLE,  selectedAdvantage.getState());
    }


    @Test
    public void setStateTest()  {
        advantageManager.addAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = selectedAdvantageManager.addSelectAdvantage(advantage,card);
        selectedAdvantageManager.setState(selectedAdvantage, StateSelectedAdvantage.NOT_AVAILABLE);
        assertEquals(selectedAdvantage.getState(),  StateSelectedAdvantage.NOT_AVAILABLE);
    }

    @Test
    public void setLastUseTest()  {
        advantageManager.addAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = selectedAdvantageManager.addSelectAdvantage(advantage,card);
        LocalDateTime localDateTime = LocalDateTime.now();
        selectedAdvantageManager.setLastUse(localDateTime,selectedAdvantage);
        assertEquals(selectedAdvantage.getLastUse(), localDateTime);
    }

    @Test
    public void getAllSelectedAdvantagesTest()  {
        advantageManager.addAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = selectedAdvantageManager.addSelectAdvantage(advantage,card);
        assertEquals(List.of(selectedAdvantage), selectedAdvantageManager.getAllSelectedAdvantages());
    }


}
