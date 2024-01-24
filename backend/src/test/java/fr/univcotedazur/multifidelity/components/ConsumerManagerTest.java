package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AccountNotFoundException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistingAccountException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
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
import java.util.Optional;

@SpringBootTest
@Commit
@Transactional
public class ConsumerManagerTest {

    @Autowired
    private ConsumerRepository consumerRepository;


    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private ConsumerManager consumerManager;

    @Autowired
    private ConsumerHandler consumerHandler;

    Consumer consumer;

    List<Consumer> consumerList;

    List<Society> societyList;

    Society society;

    Long id;

    @BeforeEach
    public void setUpContext() {
        consumerList = new ArrayList<>();
        societyList = new ArrayList<>();
        consumer = new Consumer("email@gmail.com", "password", "9384758475845847", "CZ932GJ",null);
        consumerList.add(consumer);
        consumer = consumerRepository.save(consumer);
        id = consumer.getId();
        society = new Society();
        society = societyRepository.save(society);
    }


    @AfterEach
    public void cleanUpContext() throws Exception{
        consumerRepository.deleteAll();
        societyRepository.deleteAll();
    }


    @Test
    @Transactional(propagation= Propagation.NEVER)
    public void newConsumerTryToLogin(){
        Consumer newConsumer = new Consumer("newEmail@gmail.com", "newPassword", null , null,null);
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            Consumer check = consumerManager.researchConsumer(newConsumer.getEmail(), newConsumer.getPassword());
            Assertions.assertEquals(1, consumerRepository.count());
            Assertions.assertEquals(newConsumer.getEmail(), check.getEmail());
            Assertions.assertEquals(newConsumer.getPassword(), check.getPassword());
        });
    }


    @Test
    public void newConsumerSignin() throws AlreadyExistingAccountException {
        Consumer newConsumer = new Consumer("newEmail@gmail.com", "newPassword", null , null,null);
        Consumer check = consumerManager.addConsumer(newConsumer);
            Assertions.assertEquals(2, consumerRepository.count());
            Assertions.assertEquals(newConsumer.getEmail(), check.getEmail());
            Assertions.assertEquals(newConsumer.getPassword(), check.getPassword());
    }


    @Test
    public void newConsumerLogin() throws AccountNotFoundException {
        Consumer newConsumer = new Consumer("newEmail@gmail.com", "newPassword", null , null,null);
        consumerRepository.save(newConsumer);
        Consumer check = consumerManager.researchConsumer(newConsumer.getEmail(), newConsumer.getPassword());
            Assertions.assertEquals(2, consumerRepository.count());
            Assertions.assertEquals(newConsumer.getEmail(), check.getEmail());
            Assertions.assertEquals(newConsumer.getPassword(), check.getPassword());

    }


    @Test
    public void consumerAddFavSociety()  {
        societyList.add(society);
        consumerManager.addFavSociety(society, consumer);
        Assertions.assertEquals(1, consumerHandler.getFavSocietiesByConsumer(consumer).size());

    }

    @Test
    public void consumerdeleteFavSociety()  {
        society = new Society();
        consumerManager.addFavSociety(society, consumer);
        consumerManager.deleteFavSociety(society,consumer);
        Assertions.assertEquals(0, consumerHandler.getFavSocietiesByConsumer(consumer).size());
    }

    @Test
    public void testGetConsumerById() {
            Consumer check = consumerManager.findConsumerById(id).get();
            Assertions.assertEquals(consumer, check);
    }

    @Test
    public void testGetAllConsumer(){
        List<Consumer> checkList = consumerManager.getAllConsumer();
        Assertions.assertEquals(consumerList, checkList);
    }

    @Test
    public void consumerDeleteFavSociety()  {
        consumerManager.deleteFavSociety(society, consumer);
        Assertions.assertEquals(0, consumerHandler.getFavSocietiesByConsumer(consumer).size());
    }

    @Test
    public void updateConsumer() throws ResourceNotFoundException {
        Consumer newConsumer = new Consumer("newMail@gmail.com", "newPassword", "123456789", "CZ932GJ",null);
        newConsumer.setId(id);
        consumerManager.updateConsumer(consumer, newConsumer);
        Assertions.assertEquals(consumer.getCreditCardNumber(), newConsumer.getCreditCardNumber());
        Assertions.assertEquals(consumer.getEmail(), newConsumer.getEmail());
        Assertions.assertEquals(consumer.getPassword(), newConsumer.getPassword());
        Assertions.assertEquals(consumer.getId(), consumer.getId());
        Assertions.assertEquals(consumer.getCreditCardNumber(), newConsumer.getCreditCardNumber());
        Assertions.assertEquals(1, consumerRepository.count());
    }


    @Test
    public void findConsumerByPlate() throws ResourceNotFoundException {
        Consumer newConsumer = new Consumer("newMail@gmail.com", "newPassword", "123456789", "CZ932G",null);
        consumerRepository.save(newConsumer);
        Optional<Consumer> consumer1 = consumerManager.findConsumerByPlate("CZ932G");
        Assertions.assertTrue(consumer1.isPresent());
        Assertions.assertEquals(consumer1.get().getLicencePlate(), "CZ932G");
    }
}
