package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AccountNotFoundException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistingAccountException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Commit
@Transactional
public class ConsumerHandlerTest {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private PurchaseManager purchaseManager;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private SocietyRepository societyRepository;

    Consumer consumer;

    @Autowired
    private ConsumerHandler consumerHandler;

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
    }


    @AfterEach
    public void cleanUpContext() throws Exception{
        purchaseRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
    }


    @Test
    @Transactional(propagation= Propagation.NEVER)
    public void newConsumerTryToSigninWithSameEmail(){
        Consumer newConsumer = new Consumer("email@gmail.com", "newPassword", null , null,null);
        Assertions.assertThrows(AlreadyExistingAccountException.class, () -> {
            Consumer check = consumerHandler.signIn(newConsumer.getEmail(), newConsumer.getPassword());
            Assertions.assertEquals(1, consumerRepository.count());
            Assertions.assertEquals(newConsumer.getEmail(), check.getEmail());
            Assertions.assertEquals(newConsumer.getPassword(), check.getPassword());
        });
    }

    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void newConsumerTryToLogin(){
        Consumer newConsumer = new Consumer("newEmail@gmail.com", "newPassword", null , null,null);
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            Consumer check = consumerHandler.logIn(newConsumer.getEmail(), newConsumer.getPassword());
        });
    }


    @Test
    public void newConsumerSignin() throws AlreadyExistingAccountException {
        Consumer newConsumer = new Consumer("newEmail@gmail.com", "newPassword", null , null,null);
        Consumer check = consumerHandler.signIn(newConsumer.getEmail(), newConsumer.getPassword());
        Assertions.assertEquals(2, consumerRepository.count());
        Assertions.assertEquals(newConsumer.getEmail(), check.getEmail());
        Assertions.assertEquals(newConsumer.getPassword(), check.getPassword());

    }


    @Test
    public void newConsumerLogin() throws AccountNotFoundException {
        Consumer newConsumer = new Consumer("newEmail@gmail.com", "newPassword", null , null,null);
        consumerRepository.save(newConsumer);
        Consumer check = consumerHandler.logIn(newConsumer.getEmail(), newConsumer.getPassword());
        Assertions.assertEquals(2, consumerRepository.count());
        Assertions.assertEquals(newConsumer.getEmail(), check.getEmail());
        Assertions.assertEquals(newConsumer.getPassword(), check.getPassword());

    }


    @Test
    public void consumerAddFavSociety() throws ResourceNotFoundException {
        consumerHandler.addFavSociety(society, consumer);
        Assertions.assertEquals(1, consumerHandler.getFavSocietiesByConsumer(consumer).size());

    }



    @Test
    public void consumerdeleteFavSociety() throws ResourceNotFoundException {
        consumerHandler.addFavSociety(society, consumer);
        consumerHandler.deleteFavSociety(society,consumer);
        Assertions.assertEquals(0, consumerHandler.getFavSocietiesByConsumer(consumer).size());
    }


    @Test
    public void testGetConsumerById() throws ResourceNotFoundException {
        Consumer check = consumerHandler.getConsumerById(consumer.getId());
        Assertions.assertEquals(consumer, check);
    }

    @Test
    public void testGetAllConsumer(){
        List<Consumer> checkList = consumerHandler.getAllConsumer();
        Assertions.assertEquals(consumerList, checkList);
    }

    @Test
    public void testGetFavSocieties() throws ResourceNotFoundException {
        List<Society> checkList = consumerHandler.getFavSocietiesByConsumer(consumer);
        Assertions.assertEquals(societyList, checkList);
    }

    @Test
    public void consumerDeleteFavSociety() throws ResourceNotFoundException {
        consumerHandler.deleteFavSociety(society, consumer);
        Assertions.assertEquals(0, consumerHandler.getFavSocietiesByConsumer(consumer).size());
    }

    @Test
    public void updateInfos() throws ResourceNotFoundException {
        Consumer newConsumer = new Consumer("newMail@gmail.com", "newPassword", "123456789", "CZ932GJ",new Card());
        newConsumer.setId(consumer.getId());
        consumerHandler.updateConsumer(consumer, newConsumer);
        Consumer check = consumerHandler.getConsumerById(consumer.getId());
        Assertions.assertEquals(check.getCreditCardNumber(), newConsumer.getCreditCardNumber());
        Assertions.assertEquals(check.getEmail(), newConsumer.getEmail());
        Assertions.assertEquals(check.getPassword(), newConsumer.getPassword());
        Assertions.assertEquals(check.getId(), consumer.getId());
        Assertions.assertEquals(check.getCreditCardNumber(), newConsumer.getCreditCardNumber());
        Assertions.assertEquals(1, consumerRepository.count());
    }


}
