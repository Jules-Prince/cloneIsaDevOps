package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Society;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
@Commit
public class AdvantageManagerTest {

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
    public void addAdvantageTest()  {
        advantageManager.addAdvantage(advantage);
        assertEquals(1,  advantageRepository.count());
    }

    @Test
    public void deleteAdvantageTest()  {
        advantageManager.addAdvantage(advantage);
        advantageManager.deleteAdvantage(advantage);
        assertEquals(0,  advantageRepository.count());
    }

    @Test
    public void getAllAdvantagesTest()  {
        advantageManager.addAdvantage(advantage);
        assertEquals(List.of(advantage), advantageManager.getAllAdvantages());
    }




    @Test
    public void getAllAdvantageBySocietyTest()  {
        advantageManager.addAdvantage(advantage);
        assertEquals(List.of(advantage), advantageManager.getAllAdvantageBySociety(society));
    }

    @Test
    public void updateAdvantageTest()  {
        advantageManager.addAdvantage(advantage);
        Advantage newAdvantage = new Advantage("café presque offert",100,true, 3.5F,society,1);
        advantageManager.updateAdvantage(advantage, newAdvantage);
        assertEquals(newAdvantage.getPrice(), advantage.getPrice());
        assertEquals(newAdvantage.getPoint(), advantage.getPoint());
        assertEquals(newAdvantage.getName(), advantage.getName());
        assertEquals(newAdvantage.isVfpAdvantage(), advantage.isVfpAdvantage());
        assertNotEquals(newAdvantage.getId(), advantage.getId());
        assertEquals(newAdvantage.getSociety(), advantage.getSociety());
        assertEquals(newAdvantage.getInitialPrice(), advantage.getInitialPrice());
        assertEquals(1, advantageRepository.count());
    }

}
