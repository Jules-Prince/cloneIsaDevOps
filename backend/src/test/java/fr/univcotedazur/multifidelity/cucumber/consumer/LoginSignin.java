package fr.univcotedazur.multifidelity.cucumber.consumer;

import fr.univcotedazur.multifidelity.components.ConsumerHandler;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.AccountNotFoundException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistingAccountException;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LoginSignin {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ConsumerHandler consumerHandler;

    Consumer consumer;

    @Before
    public void beforeContext() throws Exception{
        consumerRepository.deleteAll();
    }


    @Given("there is a consumer with email {string} and password {string} in database")
    public void consumer_with_email_and_password(String email, String password){
        consumer = new Consumer();
        consumer.setEmail(email);
        consumer.setPassword(password);
        consumer = consumerRepository.save(consumer);
    }

    @When("the consumer signin")
    public void theConsumerSignin() {
        try {
            consumer = new Consumer("claire@orange.fr", "passwrd", null, null,null);
            consumerHandler.signIn(consumer.getEmail(),consumer.getPassword());
        } catch (AlreadyExistingAccountException e) {
            System.out.println(e.getMessage());
        }
    }

    @Then("there is {int} consumer in the database")
    public void thereIsConsumerInTheDatabase(int arg0) {
        Assertions.assertEquals(arg0, consumerRepository.count());
    }

    @When("a new consumer login with email {string} and password {string}")
    public void theNewConsumerLogin(String email, String password) {
        try {
            consumerHandler.signIn(email, password);
        } catch(AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @When("a new consumer signin with email {string} and password {string}")
    public void theNewConsumerSignin(String email, String password) {
        try {
            consumerHandler.signIn(email,password);
        } catch (AlreadyExistingAccountException e) {
            System.out.println(e.getMessage());
        }
    }
}
