package fr.univcotedazur.multifidelity.cli.commands;

import fr.univcotedazur.multifidelity.cli.CliContext;
import fr.univcotedazur.multifidelity.cli.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalTime;

@ShellComponent
public class SocietyCommands {

    public static final String BASE_URI = "/societies";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @Autowired
    PartnerCommands partnerCommands;

    @Autowired
    public SocietyCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @ShellMethod("Create a society in the backend(createSociety NAME OWNER_EMAIL OPENING_HOUR CLOSING_HOUR ADRESS)")
    public CliSociety createSociety(String name, String ownerEmail, LocalTime openingHour, LocalTime closingHour, String address){
        CliPartner cliPartner = cliContext.getPartners().get(ownerEmail);
        Long idCliPartner = cliPartner.getId();
        CliSociety cliSociety = new CliSociety(name, cliPartner.getId(), openingHour, closingHour, address);
        cliSociety = restTemplate.postForObject(BASE_URI , cliSociety, CliSociety.class);
        cliContext.getSocieties().put(cliSociety.getName(), cliSociety);
        return cliSociety;
    }

    @ShellMethod("Update a society in the backend(updateSociety SOCIETY_NAME OWNER_EMAIL OPENING_HOUR CLOSING_HOUR ADRESS)")
    public CliSociety updateSociety(String societyName, String ownerEmail, LocalTime openingHour, LocalTime closingHour, String address){
        CliPartner cliPartner = cliContext.getPartners().get(ownerEmail);
        Long societyId = cliContext.getSocieties().get(societyName).getId();
        CliSociety cliSociety = new CliSociety(societyName, cliPartner.getId(), openingHour, closingHour, address);
        restTemplate.put(BASE_URI + "/" + societyId, cliSociety, CliSociety.class);
        cliContext.getSocieties().put(cliSociety.getName(), cliSociety);
        return cliSociety;
    }

    @ShellMethod("Delete a society in the backend(deleteSociety SOCIETY_ID)")
    public void deleteSociety(String societyName){
        String societyId = cliContext.getSocieties().get(societyName).getId().toString();
        restTemplate.delete(BASE_URI + "/" + societyId);
        cliContext.getSocieties().remove(societyId);
    }

    @ShellMethod("Get all societies registered (societies)")
    public String societies() {
        return cliContext.getSocieties().toString();
    }

    @ShellMethod("Get a society by its id (societyById SOCIETY_ID)")
    public CliSociety societyById(Long societyId) {
        CliSociety cliSociety = restTemplate.getForObject(BASE_URI + "/" + societyId, CliSociety.class);
        return cliSociety;
    }
}
