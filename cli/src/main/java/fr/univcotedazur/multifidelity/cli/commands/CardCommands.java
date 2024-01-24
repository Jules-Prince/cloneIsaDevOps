package fr.univcotedazur.multifidelity.cli.commands;

import fr.univcotedazur.multifidelity.cli.CliContext;
import fr.univcotedazur.multifidelity.cli.model.CliCard;
import fr.univcotedazur.multifidelity.cli.model.CliConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;


@ShellComponent
public class CardCommands {

    public static final String BASE_URI = "/card";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @Autowired
    ConsumerCommands consumerCommands;

    @Autowired
    public CardCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Create a card in the backend(createCard POINT BALANCE LIMIT_PAYMENT OWNER_ID)")
    public CliCard createCard(String ownerEmail){
        CliConsumer cliConsumer = cliContext.getConsumers().get(ownerEmail);
        Long idCliConsumer = cliConsumer.getId();
        CliCard cliCard = new CliCard();
        cliCard = restTemplate.postForObject(BASE_URI + "/" + idCliConsumer + "/card", cliCard, CliCard.class);
        cliConsumer.setCardId(cliCard.getId());
        cliCard.setOwnerId(cliConsumer.getId());
        cliContext.getCards().put(cliConsumer.getEmail(), cliCard);
        cliContext.getConsumers().put(cliConsumer.getEmail(), cliConsumer);
        return cliCard;
    }

    @ShellMethod("Update a card in the backend(updateCard POINT BALANCE LIMIT_PAYMENT OWNER_EMAIL)")
    public CliCard updateCard(int point, float balance, int limitPayment, String ownerEmail){
        CliConsumer cliConsumer = cliContext.getConsumers().get(ownerEmail);
        Long id = cliContext.getCards().get(ownerEmail).getId();
        CliCard cliCard = new CliCard(id, point, balance, limitPayment, cliConsumer.getCardId());
        restTemplate.put(BASE_URI + "/" + id, cliCard);
        cliContext.getCards().put(cliConsumer.getEmail(), cliCard);
        return cliCard;
    }

    @ShellMethod("Delete a card in the backend(deleteCard OWNER_CARD)")
    public void deleteCard(String ownerName){
        String id = cliContext.getCards().get(ownerName).getId().toString();
        restTemplate.delete(BASE_URI + "/" + id);
        cliContext.getCards().remove(ownerName);
    }

    @ShellMethod("Get all cards registered (cards)")
    public String cards() {
        return cliContext.getCards().toString();
    }

    @ShellMethod("Get a card by its id (cardById CARD_ID)")
    public CliCard cardById(Long cardId) {
        CliCard cliCard = restTemplate.getForObject(BASE_URI + "/" + cardId, CliCard.class);
        return cliCard;
    }

    @ShellMethod("Get a card by its owner id (cardByOwnerId OWNER_EMAIL)")
    public CliCard cardByOwnerId(String ownerEmail) {
        Long ownerId = cliContext.getConsumers().get(ownerEmail).getId();
        CliCard cliCard = restTemplate.getForObject(BASE_URI + "/owner/" + ownerId, CliCard.class);
        return cliCard;
    }


    @ShellMethod("Reload balance of the card (reloadBalance CARD_ID AMOUNT)")
    public Float reloadBalance(Float amount, String ownerEmail) {
        Long ownerId = cliContext.getConsumers().get(ownerEmail).getId();
        restTemplate.put(BASE_URI + "/" + ownerId + "/balance", amount, Float.class);
        CliCard card =  cliContext.getCards().get(ownerEmail);
        card = cardById(card.getId());
        cliContext.getCards().put(ownerEmail, card);
        return amount;
    }

    @ShellMethod("Get balance of the card (getBalance CARD_ID)")
    public CliCard getBalance(String ownerEmail) {
        Long ownerId = cliContext.getConsumers().get(ownerEmail).getId();
        CliCard cliCard = restTemplate.getForObject(BASE_URI + "/" + ownerId + "/balance", CliCard.class);
        return cliCard;
    }
}
