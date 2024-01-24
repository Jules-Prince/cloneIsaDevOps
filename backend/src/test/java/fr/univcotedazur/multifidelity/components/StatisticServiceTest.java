package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
import fr.univcotedazur.multifidelity.repositories.SelectedAdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@Commit
public class StatisticServiceTest {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ConsumerRepository consumerRepository;


    @Autowired
    AdvantageRepository advantageRepository;

    @Autowired
    SelectedAdvantageRepository selectedAdvantageRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    SocietyRepository societyRepository;

    @Autowired
    StatisticService statisticService;

    @Autowired
    AdvantageManager advantageManager;

    Society society;
    Card card;
    Consumer consumer;
    Long id;
    Advantage advantage;

    @BeforeEach
    public void setUpContext() {
        card = new Card(0,0f);
        card =cardRepository.save(card);
        consumer = new Consumer("email@gmail.com", "password", "9384758475845847", "CZ932GJ",null);
        consumer.setCard(card);
        consumer = consumerRepository.save(consumer);
        id = consumer.getId();
        society = new Society();
        society =societyRepository.save(society);
    }


    @AfterEach
    public void cleanUpContext() throws Exception{;
        selectedAdvantageRepository.deleteAll();
        purchaseRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @Test
    public void testGetProfitBySocietyFreeGift(){
        advantage = new Advantage("café offert", 2, false, 1, society, 0);
        advantage =advantageRepository.save(advantage);
        for(int i=0;i<14;i++){
            Purchase purchase = new Purchase(2, LocalDateTime.now().minusDays(2),society,card);
            purchaseRepository.save(purchase);
        }
        for(int i=0;i<14;i++){
            SelectedAdvantage newSelectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, advantage, LocalDateTime.now().minusDays(1));
            selectedAdvantageRepository.save(newSelectedAdvantage);
        }

        float check = statisticService.getProfitBySociety(society);
        Assertions.assertEquals(14, check);
    }

    @Test
    public void testGetProfitBySocietyReduction(){
        advantage = new Advantage("café moitié prix", 2, false, 2, society, 1);
        advantage =advantageRepository.save(advantage);
        for(int i=0;i<14;i++){
            Purchase purchase = new Purchase(2, LocalDateTime.now().minusDays(2),society,card);
            purchaseRepository.save(purchase);
        }
        for(int i=0;i<14;i++){
            SelectedAdvantage newSelectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, advantage, LocalDateTime.now().minusDays(1));
            selectedAdvantageRepository.save(newSelectedAdvantage);
        }

        float check = statisticService.getProfitBySociety(society);
        Assertions.assertEquals(28, check);
    }


    @Test
    public void testGetProfitFreeGift(){
        advantage = new Advantage("café offert", 2, false, 1, society,  0);
        advantage =advantageRepository.save(advantage);
        for(int i=0;i<14;i++){
            Purchase purchase = new Purchase(2, LocalDateTime.now().minusDays(2),society,card);
            purchaseRepository.save(purchase);
        }
        for(int i=0;i<14;i++){
            SelectedAdvantage newSelectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, advantage, LocalDateTime.now().minusDays(1));
            selectedAdvantageRepository.save(newSelectedAdvantage);
        }

        float check = statisticService.getProfit();
        Assertions.assertEquals(14, check);
    }

    @Test
    public void testGetProfitReduction() {
        advantage = new Advantage("café moitié prix", 2, false, 2, society, 1);
        advantage =advantageRepository.save(advantage);
        for (int i = 0; i < 14; i++) {
            Purchase purchase = new Purchase(2, LocalDateTime.now().minusDays(2), society, card);
            purchaseRepository.save(purchase);
        }
        for (int i = 0; i < 14; i++) {
            SelectedAdvantage newSelectedAdvantage = new SelectedAdvantage( StateSelectedAdvantage.AVAILABLE, card, advantage, LocalDateTime.now().minusDays(1));
            selectedAdvantageRepository.save(newSelectedAdvantage);
        }

        float check = statisticService.getProfitBySociety(society);
        Assertions.assertEquals(28, check);
    }


    @Test
    public void testGetFrequencyPurchaseBySociety(){
        for(int i=0;i<14;i++){
            Purchase purchase = new Purchase(i+1, LocalDateTime.now().minusDays(2),society,card);
            purchaseRepository.save(purchase);
        }
        float check = statisticService.getFrequencyPurchaseBySociety(society);
        Assertions.assertEquals(2, check);
    }

    @Test
    public void testGetFrequencyPurchase(){
        for(int i=0;i<14;i++){
            Purchase purchase = new Purchase(i+1, LocalDateTime.now().minusDays(2),society,card);
            purchaseRepository.save(purchase);
        }
        float check = statisticService.getFrequencyPurchaseBySociety(society);
        Assertions.assertEquals(2, check);
    }

}
