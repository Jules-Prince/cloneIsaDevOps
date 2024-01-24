package fr.univcotedazur.multifidelity.cucumber.consumer;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.ConsumerFinder;
import fr.univcotedazur.multifidelity.interfaces.ConsumerModifier;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class updateConsumer {

    @Autowired
    ConsumerRepository  consumerRepository;

    @Autowired
    ConsumerFinder consumerFinder;

    @Autowired
    ConsumerModifier consumerModifier;

    Consumer consumer;

    Long consumerId;

    @Before
    public void beforeContext() throws Exception{
        consumerRepository.deleteAll();
    }


    @Given("a consumer with email {string} and password {string} and credit card number {string} and licence plate {string}")
    public void aConsumerWithEmailAndPasswordAndCreditCardNumberAndLicencePlate(String arg0, String arg1, String arg2, String arg3) {
        consumer = new Consumer(arg0, arg1, arg2, arg3,null);
        consumerId = consumerRepository.save(consumer).getId();
    }

    @When("the consumer update his email with {string} and password with {string} and credit card number with {string} and licence plate with {string}")
    public void theConsumerUpdateHisEmailWithAndPasswordWithAndCreditCardNumberWithAndLicencePlateWith(String arg0, String arg1, String arg2, String arg3) throws ResourceNotFoundException {
        Consumer newConsumer = new Consumer(arg0, arg1, arg2, arg3,null);
        consumer = consumerFinder.findConsumerById(consumerId).get();
        newConsumer.setId(consumer.getId());
        consumerModifier.updateConsumer(consumer, newConsumer);
    }

    @Then("the consumer informations are : email {string} and password {string} and credit card number {string} and licence plate {string}")
    public void theConsumerInformationsAreEmailAndPasswordAndCreditCardNumberAndLicencePlate(String arg0, String arg1, String arg2, String arg3) {
        consumer = consumerFinder.findConsumerById(consumerId).get();
        assertEquals(arg0, consumer.getEmail());
        assertEquals(arg1, consumer.getPassword());
        assertEquals(arg2, consumer.getCreditCardNumber());
        assertEquals(arg3, consumer.getLicencePlate());
        assertEquals(consumerRepository.count(), 1);
    }
}
