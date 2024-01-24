package fr.univcotedazur.multifidelity.cli.commands;

import fr.univcotedazur.multifidelity.cli.CliContext;
import fr.univcotedazur.multifidelity.cli.exceptions.RessourceNotFoundException;
import fr.univcotedazur.multifidelity.cli.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@ShellComponent
public class ConsumerCommands {
    public static final String BASE_URI = "/consumers";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @Autowired
    AdvantageCommands advantageCommands;

    @Autowired
    public ConsumerCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Create a customer in the backend(signin CUSTOMER_EMAIL PASSWORD)")
    public CliConsumer signin(String email, String password){
        CliConsumer cliConsumer = new CliConsumer(email, password, null, null);
        cliConsumer = restTemplate.postForObject(BASE_URI + "/sign-in", cliConsumer, CliConsumer.class);
        cliContext.getConsumers().put(cliConsumer.getEmail(), cliConsumer);
        return cliConsumer;
    }

    @ShellMethod("Delete a customer in the backend(delete ID_EMAIL)")
    public void delete(String email){
        Long idCliConsumer = cliContext.getConsumers().get(email).getId();
        restTemplate.delete(BASE_URI + "/{idCliConsumer}", idCliConsumer);
        cliContext.getConsumers().remove(email);
    }

    @ShellMethod("Login a customer (login CUSTOMER_EMAIL PASSWORD)")
    public CliConsumer login(String email, String password){
        CliConsumer cliConsumer = new CliConsumer(email, password);
        cliConsumer = restTemplate.postForObject(BASE_URI + "/log-in", cliConsumer, CliConsumer.class);
        cliContext.getConsumers().put(cliConsumer.getEmail(), cliConsumer);
        return cliConsumer;
    }

    @ShellMethod("Update a customer in the backend(update ID_EMAIL PASSWORD CREDIT_CARD_NUMBER LICENCE_PLATE)")
    public CliConsumer updateConsumer(String email, String password, String creditCardNumber, String licencePlate){
        Long idCliConsumer = cliContext.getConsumers().get(email).getId();
        CliConsumer cliConsumer = new CliConsumer(email, password, creditCardNumber, licencePlate);
        cliConsumer.setId(idCliConsumer);
        restTemplate.put(BASE_URI + "/" + idCliConsumer, cliConsumer);
        cliContext.getConsumers().put(cliConsumer.getEmail(), cliConsumer);
        return cliConsumer;
    }

    @ShellMethod("Get all consumers registered (consumers)")
    public String consumers() {
        return cliContext.getConsumers().toString();
    }

    @ShellMethod("Delete all consumers registered (deleteConsumers)")
    public void deleteConsumers() {
        restTemplate.delete(BASE_URI);
        cliContext.getConsumers().clear();
    }

    @ShellMethod("Get a consumer by its id (consumerById CONSUMER_ID)")
    public CliConsumer consumerById(Long consumerId) {
        CliConsumer cliConsumer = restTemplate.getForObject(BASE_URI + "/" + consumerId, CliConsumer.class);
        return cliConsumer;
    }

    @ShellMethod("Get a consumer by its email (consumerByEmail CONSUMER_EMAIL)")
    public CliConsumer consumerByEmail(String consumerEmail) {
        CliConsumer cliConsumer = cliContext.getConsumers().get(consumerEmail);
        return cliConsumer;
    }

}
