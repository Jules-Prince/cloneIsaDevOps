package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.NotAvailableAdvantageException;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughPointException;
import fr.univcotedazur.multifidelity.interfaces.SelectedAdvantageGetter;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.SelectedAdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import fr.univcotedazur.multifidelity.repositories.ValidityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
public class CatalogTest {

    @Autowired
    private SelectedAdvantageRepository selectedAdvantageRepository;

    @Autowired
    private AdvantageRepository advantageRepository;


    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ValidityRepository validityRepository;


    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private Catalog catalog;

    @Autowired
    private GiftService advantageSelecter;


    private Advantage advantage;

    private Society society;

    private Consumer consumer;


    private Long consumerId;


    private Card card;


    private Long cardId;

    @Autowired
    private SelectedAdvantageGetter advantageAvailability;


    @BeforeEach
    public void setUpContext() {
        society = new Society();
        society = societyRepository.save(society);
        card = new Card(20, 20);
        card = cardRepository.save(card);
        cardId = card.getId();
        consumer = new Consumer();
        consumer.setCard(card);
        consumerId = consumerRepository.save(consumer).getId();
        advantage = new Advantage("café offert", 12, false, 2.5F, society, 0);
    }


    @AfterEach
    public void cleanUpContext() throws Exception {
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        validityRepository.deleteAll();
        societyRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @Test
    public void addAdvantageTest() throws AlreadyExistSimilarRessourceException {
        catalog.createAdvantage(advantage);
        assertEquals(1, advantageRepository.count());
    }

    @Test
    public void deleteAdvantageTest() throws AlreadyExistSimilarRessourceException {
        catalog.createAdvantage(advantage);
        catalog.deleteAdvantage(advantage);
        assertEquals(0, advantageRepository.count());
    }

    @Test
    public void addSelectAdvantageTest() throws AlreadyExistSimilarRessourceException, NotEnoughPointException {
        catalog.createAdvantage(advantage);
        consumer = consumerRepository.findById(consumerId).get();
        SelectedAdvantage selectedAdvantage = advantageSelecter.selectAdvantage(advantage, consumer);
        assertEquals(1, selectedAdvantageRepository.count());
        assertEquals(8, card.getPoint());
        assertEquals(StateSelectedAdvantage.AVAILABLE, selectedAdvantage.getState());
    }


    @Test
    public void addSelectAdvantageTestNotEnoughPointException() throws AlreadyExistSimilarRessourceException, NotEnoughPointException {
        advantage.setPoint(100);
        consumer = consumerRepository.findById(consumerId).get();
        catalog.createAdvantage(advantage);
        Assertions.assertThrows(NotEnoughPointException.class, () -> advantageSelecter.selectAdvantage(advantage, consumer));
    }


    @Test
    public void getAllAdvantagesTest() throws AlreadyExistSimilarRessourceException {
        catalog.createAdvantage(advantage);
        assertEquals(List.of(advantage), catalog.getAllAdvantages());
    }


    @Test
    public void giveGiftTest() throws AlreadyExistSimilarRessourceException, NotEnoughPointException, NotAvailableAdvantageException {
        consumer = consumerRepository.findById(consumerId).get();
        catalog.createAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = advantageSelecter.selectAdvantage(advantage, consumer);
        SelectedAdvantage newSelectedAdvantage = advantageSelecter.useAdvantage(selectedAdvantage);
        assertEquals(newSelectedAdvantage.getState(), StateSelectedAdvantage.NOT_AVAILABLE);
    }

    @Test
    public void giveGiftTestNotAvailableAdvantageException() throws AlreadyExistSimilarRessourceException, NotEnoughPointException {
        consumer = consumerRepository.findById(consumerId).get();
        catalog.createAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = advantageSelecter.selectAdvantage(advantage, consumer);
        selectedAdvantage.setState(StateSelectedAdvantage.NOT_AVAILABLE);
        Assertions.assertThrows(NotAvailableAdvantageException.class, () ->   advantageSelecter.useAdvantage(selectedAdvantage));
    }


    @Test
    public void getAllSelectedAdvantagesTest() throws NotEnoughPointException {
        consumer = consumerRepository.findById(consumerId).get();
        catalog.createAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = advantageSelecter.selectAdvantage(advantage, consumer);
        assertEquals(List.of(selectedAdvantage), advantageAvailability.getAllSelectedAdvantages());
    }


    @Test
    public void getAllAdvantageBySocietyTest() throws AlreadyExistSimilarRessourceException {
        catalog.createAdvantage(advantage);
        assertEquals(List.of(advantage), catalog.getAllAdvantagesBySociety(society));
    }

    @Test
    public void updateAdvantageTest() {
        catalog.createAdvantage(advantage);
        Advantage newAdvantage = new Advantage("café presque offert", 100, true, 3.5F, society, 1);
        catalog.updateAdvantage(advantage, newAdvantage);
        assertEquals(newAdvantage.getPrice(), advantage.getPrice());
        assertEquals(newAdvantage.getPoint(), advantage.getPoint());
        assertEquals(newAdvantage.getName(), advantage.getName());
        assertEquals(newAdvantage.isVfpAdvantage(), advantage.isVfpAdvantage());
        assertNotEquals(newAdvantage.getId(), advantage.getId());
        assertEquals(newAdvantage.getSociety(), advantage.getSociety());
        assertEquals(newAdvantage.getInitialPrice(), advantage.getInitialPrice());
    }

    @Test
    public void getAllAdvantageEligibleByConsumerSeeAllTest() throws AlreadyExistSimilarRessourceException {
        catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(100);
        consumer.setCard(card);
        assertEquals(List.of(advantage), catalog.getAllAdvantageEligibleByConsumer(consumer));
    }

    @Test
    public void getAllAdvantageEligibleByConsumerSeeOnlyVfpTest() throws AlreadyExistSimilarRessourceException {
        advantage.setVfpAdvantage(true);
        catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(false);
        card.setPoint(100);
        consumer.setCard(card);
        assertEquals(List.of(), catalog.getAllAdvantageEligibleByConsumer(consumer));
    }

    @Test
    public void getAllAdvantageEligibleByConsumerSeeOnlyPointTest() throws AlreadyExistSimilarRessourceException {
        advantage.setPoint(100);
        catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(10);
        consumer.setCard(card);
        assertEquals(List.of(), catalog.getAllAdvantageEligibleByConsumer(consumer));
    }

}
