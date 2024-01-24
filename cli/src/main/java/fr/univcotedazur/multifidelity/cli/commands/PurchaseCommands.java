package fr.univcotedazur.multifidelity.cli.commands;

import fr.univcotedazur.multifidelity.cli.CliContext;
import fr.univcotedazur.multifidelity.cli.model.CliCard;
import fr.univcotedazur.multifidelity.cli.model.CliConsumer;
import fr.univcotedazur.multifidelity.cli.model.CliPurchase;
import fr.univcotedazur.multifidelity.cli.model.CliSociety;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class PurchaseCommands {

    public static final String BASE_URI = "/purchases";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @Autowired
    SocietyCommands societyCommands;

    @Autowired
    CardCommands cardCommands;

    @Autowired
    ConsumerCommands consumerCommands;


    @Autowired
    public PurchaseCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Create a purchase in the backend(createPurchase AMOUNT SOCIETY_NAME CARD_OWNER_EMAIL)")
    public CliPurchase createPurchase(float amount, String societyName, String ownerEmail){
        CliSociety cliSociety = cliContext.getSocieties().get(societyName);
        CliCard cliCard = cliContext.getCards().get(ownerEmail);
        CliPurchase cliPurchase = new CliPurchase(amount, null, cliSociety.getId(), cliCard.getId());
        cliPurchase = restTemplate.postForObject(BASE_URI, cliPurchase, CliPurchase.class);
        cliContext.getPurchases().put(cliPurchase.getId(), cliPurchase);
        CliCard card = cardCommands.cardById(cliCard.getId());
        cliContext.getCards().put(ownerEmail, card);
        CliConsumer cliConsumer = consumerCommands.consumerById(card.getOwnerId());
        cliContext.getConsumers().put(ownerEmail, cliConsumer);
        return cliPurchase;
    }

    @ShellMethod("Create purchase pay with card (create-purchase-with-card AMOUNT SOCIETY_NAME CARD_OWNER_EMAIL)")
    public CliPurchase createPurchaseWithCard(float amount, String societyName, String ownerEmail){
        CliSociety cliSociety = cliContext.getSocieties().get(societyName);
        CliCard cliCard = cliContext.getCards().get(ownerEmail);
        CliPurchase cliPurchase = new CliPurchase(amount, null, cliSociety.getId(), cliCard.getId());
        cliPurchase = restTemplate.postForObject(BASE_URI + "/with-card", cliPurchase, CliPurchase.class);
        cliContext.getPurchases().put(cliPurchase.getId(), cliPurchase);
        CliCard card = cardCommands.cardById(cliCard.getId());
        cliContext.getCards().put(ownerEmail, card);
        CliConsumer cliConsumer = consumerCommands.consumerById(card.getOwnerId());
        cliContext.getConsumers().put(ownerEmail, cliConsumer);
        return cliPurchase;
    }

    @ShellMethod("Delete a purchase in the backend(deletePurchase PURCHASE_ID)")
    public void deletePurchase(String purchaseId){
        restTemplate.delete(BASE_URI + "/" + purchaseId);
        cliContext.getPurchases().remove(purchaseId);
    }

    @ShellMethod("Get a purchase by its id (purchaseById PURCHASE_ID)")
    public CliPurchase purchaseById(String purchaseId) {
        CliPurchase cliPurchase = restTemplate.getForObject(BASE_URI + "/" + purchaseId, CliPurchase.class);
        return cliPurchase;
    }

    @ShellMethod("Get all purchases registered (purchases)")
    public String purchases() {
        return cliContext.getPurchases().toString();
    }

    @ShellMethod("Get purchases by consumer id (purchasesByConsumer CONSUMER_ID)")
    public String purchasesByConsumer(String consumerId) {
        return restTemplate.getForObject(BASE_URI + "/consumer/" + consumerId, String.class);
    }

    @ShellMethod("Get purchases by society id (purchasesBySociety SOCIETY_ID)")
    public String purchasesBySociety(String societyId) {
        return restTemplate.getForObject(BASE_URI + "/society/" + societyId, String.class);
    }
}
