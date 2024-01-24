package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.connectors.ISWYPLS_Proxy;
import fr.univcotedazur.multifidelity.connectors.externaldto.ParcDto;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Validity;
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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@SpringBootTest
@Transactional
@Commit
public class GiftServiceTest {

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
    private GiftService giftService;


    @Mock
    private ISWYPLS_Proxy iswypls_proxy;


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
        consumer.setLicencePlate("plate");
        consumerId = consumerRepository.save(consumer).getId();
        advantage = new Advantage("café offert", 12, false, 2.5F, society, 0);
        giftService.setIsawWhereYouParkedLastSummer(iswypls_proxy);
    }


    @AfterEach
    public void cleanUpContext() throws Exception {
        selectedAdvantageRepository.deleteAll();
       // validityRepository.deleteAll();
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
    }


    @Test
    public void giveGiftTest1() throws AlreadyExistSimilarRessourceException, NotEnoughPointException, NotAvailableAdvantageException {
        consumer = consumerRepository.findById(consumerId).get();
        catalog.createAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        assertEquals(selectedAdvantage.getState(), StateSelectedAdvantage.AVAILABLE);
    }



    @Test
    public void giveGiftTest() throws AlreadyExistSimilarRessourceException, NotEnoughPointException, NotAvailableAdvantageException {
        consumer = consumerRepository.findById(consumerId).get();
        catalog.createAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        SelectedAdvantage newSelectedAdvantage = giftService.useAdvantage(selectedAdvantage);
        assertEquals(newSelectedAdvantage.getState(), StateSelectedAdvantage.NOT_AVAILABLE);
    }

    @Test
    public void giveGiftTestNotAvailableAdvantageException() throws AlreadyExistSimilarRessourceException, NotEnoughPointException {
        consumer = consumerRepository.findById(consumerId).get();
        catalog.createAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setState(StateSelectedAdvantage.NOT_AVAILABLE);
        Assertions.assertThrows(NotAvailableAdvantageException.class, () ->   giftService.useAdvantage(selectedAdvantage));
    }


    @Test
    public void getAllSelectedAdvantagesTest() throws NotEnoughPointException {
        consumer = consumerRepository.findById(consumerId).get();
        catalog.createAdvantage(advantage);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
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

    @Test
    public void useAdvantageGiftTest() {
        catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(20);
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);

        giftService.useAdvantage(selectedAdvantage);
        assertEquals(StateSelectedAdvantage.NOT_AVAILABLE, selectedAdvantage.getState());
        assertNull(selectedAdvantage.getAdvantage().getValidity());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), selectedAdvantage.getLastUse().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void notAvailableAdvantageGiftTest() {
        catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(20);
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        giftService.useAdvantage(selectedAdvantage);
        assertThrows(NotAvailableAdvantageException.class, () -> giftService.useAdvantage(selectedAdvantage));
    }

    @Test
    public void useAdvantageGiftWithDurationTest() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        advantage.setValidity( validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(20);
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setCreation(LocalDateTime.now().minusDays(5));
        selectedAdvantage.setNumberOfUse(0);
        giftService.useAdvantage(selectedAdvantage);
        assertEquals(StateSelectedAdvantage.AVAILABLE, selectedAdvantage.getState());
        assertEquals(1 , selectedAdvantage.getNumberOfUse());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), selectedAdvantage.getLastUse().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void useAdvantageGiftWithDurationExpireTest() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(1));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        advantage.setValidity( validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(20);
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setCreation(LocalDateTime.now().minusDays(2));
        selectedAdvantage.setNumberOfUse(0);
        assertThrows(NotAvailableAdvantageException.class, () -> giftService.useAdvantage(selectedAdvantage));
    }

    @Test
    public void useAdvantageGiftWithDurationToManyUseTest() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(1);
        advantage.setValidity( validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(20);
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setCreation(LocalDateTime.now().minusDays(5));
        selectedAdvantage.setLastUse(LocalDateTime.now().minusHours(1));
        selectedAdvantage.setNumberOfUse(1);
        assertThrows(NotAvailableAdvantageException.class, () -> giftService.useAdvantage(selectedAdvantage));
    }


    @Test
    public void addValidityToAdvantageTest() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(1);
        advantage.setValidity( validity);
        advantage = catalog.createAdvantage(advantage);
        assertEquals(advantage.getValidity(), validity);
    }

    @Test
    public void addValidityToAdvantageThatAlreadyHaveTest() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(1);
        Validity validity2 = new Validity();
        validity2.setDuration(Duration.ofDays(10));
        validity2.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity2.setNumberOfUse(1);
        advantage.setValidity( validity2);
        advantage = catalog.createAdvantage(advantage);
        assertEquals(advantage.getValidity(), validity2);
    }

    @Test
    public void getAllSelectedAdvantageEligibleTest() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        advantage.setValidity(validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(20);
        card.setConsumer(consumer);
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setCreation(LocalDateTime.now().minusDays(5));
        selectedAdvantage.setNumberOfUse(0);
        assertEquals(advantageAvailability.getAllSelectedAdvantageEligibleByConsumer(consumer), Arrays.asList(selectedAdvantage));
    }

    @Test
    public void getAllSelectedAdvantageEligibleTest2() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(0));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        advantage.setValidity(validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(20);
        card.setConsumer(consumer);
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setCreation(LocalDateTime.now().minusDays(5));
        selectedAdvantage.setNumberOfUse(1);
        assertEquals(advantageAvailability.getAllSelectedAdvantageEligibleByConsumer(consumer), Arrays.asList());
    }


    @Test
    public void useParkingTest() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(0));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setParc(true);
        advantage.setValidity(validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setLicencePlate("plate");
        card.setPoint(20);
        card.setConsumer(consumer);
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        when(iswypls_proxy.startSession(consumer)).thenReturn(true);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        assertEquals(selectedAdvantage.getState(), StateSelectedAdvantage.AVAILABLE);
        giftService.useParking(selectedAdvantage);
        assertEquals(selectedAdvantage.getState(), StateSelectedAdvantage.IN_PROCESSES);
    }



    @Test
    public void terminateAdvantageTest() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setParc(true);
        advantage.setValidity(validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setLicencePlate("plate");
        card.setPoint(20);
        card.setConsumer(consumer);
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setCreation(LocalDateTime.now().minusDays(5));
        assertEquals(selectedAdvantage.getState(), StateSelectedAdvantage.AVAILABLE);
        when(iswypls_proxy.startSession(consumer)).thenReturn(true);
        selectedAdvantage = giftService.useAdvantage(selectedAdvantage);
        assertEquals(selectedAdvantage.getState(), StateSelectedAdvantage.IN_PROCESSES);
        giftService.terminateAdvantage(consumer,new ParcDto("plate",0));
        assertEquals(selectedAdvantage.getState(), StateSelectedAdvantage.AVAILABLE);
    }


    @Test
    public void terminateAdvantageTest2() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setParc(true);
        advantage.setValidity(validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setLicencePlate("plate");
        card.setPoint(20);
        card.setConsumer(consumer);
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setCreation(LocalDateTime.now().minusDays(5));
        assertEquals(selectedAdvantage.getState(), StateSelectedAdvantage.AVAILABLE);
        when(iswypls_proxy.startSession(consumer)).thenReturn(true);
        selectedAdvantage = giftService.useAdvantage(selectedAdvantage);
        assertEquals(selectedAdvantage.getState(), StateSelectedAdvantage.IN_PROCESSES);
        giftService.terminateAdvantage(consumer,new ParcDto("plate",5));
        assertEquals(selectedAdvantage.getState(), StateSelectedAdvantage.IN_PROCESSES);
    }


    @Test
    public void checkValidityDurationTrue() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        advantage.setValidity(validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setVfp(true);
        card.setPoint(20);
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setCreation(LocalDateTime.now().minusDays(5));
        Assertions.assertTrue(giftService.checkValidityDuration(selectedAdvantage));
    }

    @Test
    public void checkValidityDurationFalse() {
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        advantage.setValidity(validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setCreation(LocalDateTime.now().minusDays(11));
        assertFalse(giftService.checkValidityDuration(selectedAdvantage));
    }


    //jamais utilisé
    @Test
    public void checkValidityNbUseTrue() {
        Validity validity = new Validity();
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(1);
        advantage.setValidity( validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setNumberOfUse(0);
        selectedAdvantage.setLastUse(LocalDateTime.now().minusDays(2));
        Assertions.assertTrue(giftService.checkValidityNbUse(selectedAdvantage));
    }


    //peut l'utiliser une fois par jour pendant une semaine
    @Test
    public void checkValidityNbUseTrue2() {
        Validity validity = new Validity();
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(7);
        advantage.setValidity( validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setNumberOfUse(1);
        selectedAdvantage.setLastUse(LocalDateTime.now().minusDays(1));
        Assertions.assertTrue(giftService.checkValidityNbUse(selectedAdvantage));
    }

    //déjà utilisé aujourd'hui
    @Test
    public void checkValidityNbUseFalse() {
        Validity validity = new Validity();
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(2);
        advantage.setValidity(validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setCard(card);
        consumerRepository.save(consumer);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setNumberOfUse(1);
        selectedAdvantage.setLastUse(LocalDateTime.now().minusHours(3));
        Assertions.assertTrue(giftService.checkValidityNbUse(selectedAdvantage));
    }

    //déjà utilisé 2 fois
    @Test
    public void checkValidityNbUseFalse2() {
        Validity validity = new Validity();
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(2);
        advantage.setValidity( validity);
        advantage = catalog.createAdvantage(advantage);
        Consumer consumer = new Consumer();
        consumer.setCard(card);
        SelectedAdvantage selectedAdvantage = giftService.selectAdvantage(advantage, consumer);
        selectedAdvantage.setNumberOfUse(2);
        selectedAdvantage.setLastUse(LocalDateTime.now().minusDays(3));
        Assertions.assertTrue(giftService.checkValidityNbUse(selectedAdvantage));
    }


}
