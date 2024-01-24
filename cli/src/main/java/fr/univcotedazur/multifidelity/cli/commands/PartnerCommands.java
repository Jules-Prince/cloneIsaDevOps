package fr.univcotedazur.multifidelity.cli.commands;

import fr.univcotedazur.multifidelity.cli.CliContext;
import fr.univcotedazur.multifidelity.cli.model.CliCard;
import fr.univcotedazur.multifidelity.cli.model.CliConsumer;
import fr.univcotedazur.multifidelity.cli.model.CliPartner;
import fr.univcotedazur.multifidelity.cli.model.CliPurchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;


@ShellComponent
public class PartnerCommands {

    public static final String BASE_URI = "/partners";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @Autowired
    public PartnerCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Create a partner account in the backend(createPartnerAccount NAME EMAIL PASSWORD)")
    public CliPartner createPartnerAccount(String name, String email, String password){
        CliPartner cliPartner = new CliPartner(name, email, password);
        cliPartner = restTemplate.postForObject(BASE_URI, cliPartner, CliPartner.class);
        cliContext.getPartners().put(cliPartner.getEmail(), cliPartner);
        return cliPartner;
    }

    @ShellMethod("Login a partner (loginPartnerAccount EMAIL PASSWORD)")
    public CliPartner loginPartnerAccount(String email, String password){
        CliPartner cliPartner = new CliPartner(email, password);
        cliPartner = restTemplate.postForObject(BASE_URI + "/login", cliPartner, CliPartner.class);
        cliContext.getPartners().put(cliPartner.getEmail(), cliPartner);
        return cliPartner;
    }

    @ShellMethod("Update a partner account in the backend(updatePartnerAccount ID_PARTNER NAME EMAIL PASSWORD)")
    public CliPartner updatePartnerAccount(String name, String email, String password){
        Long id = cliContext.getPartners().get(email).getId();
        CliPartner cliPartner = new CliPartner(id, name, email, password);
        restTemplate.put(BASE_URI + "/" + id, cliPartner);
        cliContext.getPartners().put(cliPartner.getEmail(), cliPartner);
        return cliPartner;
    }

    @ShellMethod("Delete a partner account in the backend(deletePartnerAccount ID_PARTNER)")
    public void deletePartnerAccount(String email){
        String id = cliContext.getPartners().get(email).getId().toString();
        restTemplate.delete(BASE_URI + "/" + id);
        cliContext.getPartners().remove(email);
    }

    @ShellMethod("Get all partners registered (partners)")
    public String partners() {
        return cliContext.getPartners().toString();
    }

    @ShellMethod("Get a partner by its id (partnerById PARTNER_ID)")
    public CliPartner partnerById(String partnerId) {
        CliPartner cliPartner = restTemplate.getForObject(BASE_URI + "/" + partnerId, CliPartner.class);
        return cliPartner;
    }
}
