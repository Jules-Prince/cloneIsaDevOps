package fr.univcotedazur.multifidelity.components;


import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Commit
@Transactional
public class VfpUnitTest {


    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private VfpUnit vfpUnit;

    @Autowired
    private PurchaseManager purchaseManager;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private SocietyRepository societyRepository;

    Consumer consumer;

    List<Consumer> consumerList;

    List<Society> societyList;

    Society society;


    Card card;

    @BeforeEach
    public void setUpContext() {
        society = societyRepository.save(new Society());
        consumerList = new ArrayList<>();
        societyList = new ArrayList<>();
        consumer = new Consumer("email@gmail.com", "password", "9384758475845847", "CZ932GJ",null);
        consumerList.add(consumer);
        card = new Card(0,0);
        card = cardRepository.save(card);
        consumer.setCard(card);
        consumer =consumerRepository.save(consumer);
        card.setConsumer(consumer);
    }


    @Test
    public void VFPCheck()  {
        Assertions.assertFalse(consumer.isVfp());
        for(int i=0;i<14;i++){
            Purchase purchase = new Purchase(i+1, LocalDateTime.now().minusDays(2), society, card);
            purchaseRepository.save(purchase);
            card.getPurchases().add(purchase);
        }
        vfpUnit.VFPCheck();
        Assertions.assertTrue(consumer.isVfp());
    }

    @Test
    public void VFPCheck2()  {
        consumer.setVfp(true);
        consumerRepository.save(consumer);
        Assertions.assertTrue(consumer.isVfp());
        for(int i=0;i<2;i++){
            Purchase purchase = new Purchase(i+1, null,society,card);
            purchaseManager.addPurchase(purchase);
            card.getPurchases().add(purchase);
        }
        vfpUnit.VFPCheck();
        Assertions.assertFalse(consumer.isVfp());
    }



    @Test
    public void VFPCheckByConsumer()  {
        Assertions.assertFalse(consumer.isVfp());
        for(int i=0;i<14;i++){
            Purchase purchase = new Purchase(i+1, LocalDateTime.now().minusDays(2),society,card);
            purchaseRepository.save(purchase);
            card.getPurchases().add(purchase);
        }
        vfpUnit.VFPCheckByConsumer(consumer);
        Assertions.assertTrue(consumer.isVfp());
    }

    @Test
    public void VFPCheckByConsumer2()  {
        consumer.setVfp(true);
        consumerRepository.save(consumer);
        Assertions.assertTrue(consumer.isVfp());
        for(int i=0;i<2;i++){
            Purchase purchase = new Purchase(i+1, null,society,card);
            purchaseManager.addPurchase(purchase);
        }
        vfpUnit.VFPCheckByConsumer(consumer);
        Assertions.assertFalse(consumer.isVfp());
    }


    @Test
    public void countPurchaseByConsumerByWeekTest() {
        for(int i=0;i<7;i++){
            Purchase purchase = new Purchase(i+1,null,society,card);
            purchaseManager.addPurchase(purchase);
            card.getPurchases().add(purchase);

        }
        assertEquals(1,vfpUnit.getFrequencyPurchaseByConsumer(consumer));
    }


    @Test
    public void getFrequencyPurchaseByConsumer2Test() {
        for(int i=0;i<10;i++){
            Purchase purchase = new Purchase(i+1, LocalDateTime.now().minusWeeks(2),society,card);
            purchaseRepository.save(purchase);
            card.getPurchases().add(purchase);
        }
        for(int i=0;i<7;i++){
            Purchase purchase = new Purchase(i+1, null,society,card);
            purchaseManager.addPurchase(purchase);
            card.getPurchases().add(purchase);
        }
        assertEquals(1,vfpUnit.getFrequencyPurchaseByConsumer(consumer));
    }


}
