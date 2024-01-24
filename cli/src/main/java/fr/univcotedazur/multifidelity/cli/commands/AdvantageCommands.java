package fr.univcotedazur.multifidelity.cli.commands;

import fr.univcotedazur.multifidelity.cli.CliContext;
import fr.univcotedazur.multifidelity.cli.model.CliAdvantage;
import fr.univcotedazur.multifidelity.cli.model.CliConsumer;
import fr.univcotedazur.multifidelity.cli.model.CliSelectedAdvantageOut;
import fr.univcotedazur.multifidelity.cli.model.CliSociety;
import fr.univcotedazur.multifidelity.cli.model.ValidityDtoOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ShellComponent
public class AdvantageCommands {
    public static final String BASE_URI = "/advantages";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @Autowired
    SocietyCommands societyCommands;

    @Autowired
    public AdvantageCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Create an advantage in the backend(createAdvantage ADVANTAGE_NAME SOCIETY_NAME POINT ISVFP INITIAL_PRICE PRICE)")
    public CliAdvantage createAdvantage(String name, String societyName, int point, String isVfpAdvantage, float initialPrice, float price){
        CliSociety cliSociety = cliContext.getSocieties().get(societyName);
        Long idSociety = cliSociety.getId();
        boolean VfpAdvantage = isVfpAdvantage.equals("true");
        CliAdvantage cliAdvantage = new CliAdvantage(name, point, VfpAdvantage, initialPrice, idSociety, price);
        cliAdvantage = restTemplate.postForObject(BASE_URI, cliAdvantage, CliAdvantage.class);
        cliContext.getAdvantages().put(cliAdvantage.getName(), cliAdvantage);
        return cliAdvantage;
    }

    @ShellMethod("Create an advantage with validity in the backend(createAdvantageWithValidity ADVANTAGE_NAME SOCIETY_NAME POINT ISVFP INITIAL_PRICE PRICE)")
    public CliAdvantage createAdvantageWithValidity(String name, String societyName, int point, String isVfpAdvantage, float initialPrice, float price, int numberOfUse, String durationPerUseBetweenUse,String duration,String isParc){
        ValidityDtoOut validityDtoOut = new ValidityDtoOut(numberOfUse,Duration.parse(durationPerUseBetweenUse),Duration.parse(duration),isParc.equals("true"));
        CliSociety cliSociety = cliContext.getSocieties().get(societyName);
        Long idSociety = cliSociety.getId();
        boolean VfpAdvantage = isVfpAdvantage.equals("true");
        CliAdvantage cliAdvantage = new CliAdvantage(name, point, VfpAdvantage, initialPrice, idSociety, price);
        cliAdvantage.setValidity(validityDtoOut);
        cliAdvantage = restTemplate.postForObject(BASE_URI, cliAdvantage, CliAdvantage.class);
        cliContext.getAdvantages().put(cliAdvantage.getName(), cliAdvantage);
        return cliAdvantage;
    }

    @ShellMethod("Update an advantage in the backend(updateAdvantage ADVANTAGE_NAME POINT ISVFP INITIAL_PRICE SOCIETY_NAME START_VALIDITY END_VALIDITY PRICE)")
    public CliAdvantage updateAdvantage(String name, int point, boolean isVfpAdvantage, float initialPrice, String societyName, LocalDateTime startValidity, LocalDateTime endValidity, float price){
        CliSociety cliSociety = cliContext.getSocieties().get(societyName);
        Long idSociety = cliSociety.getId();
        Long id = cliContext.getAdvantages().get(name).getId();
        CliAdvantage cliAdvantage = new CliAdvantage(id, name, point, isVfpAdvantage, initialPrice, idSociety, price,null);
        restTemplate.put(BASE_URI + "/" + id, cliAdvantage);
        cliContext.getAdvantages().put(cliAdvantage.getName(), cliAdvantage);
        return cliAdvantage;
    }

    @ShellMethod("Delete an advantage in the backend(deleteAdvantage NAME_ADVANTAGE)")
    public void deleteAdvantage(String name){
        String id = cliContext.getAdvantages().get(name).getId().toString();
        restTemplate.delete(BASE_URI + "/" + id);
        cliContext.getAdvantages().remove(name);
    }

    @ShellMethod("Get all advantages registered (advantages)")
    public String advantages() {
        return cliContext.getAdvantages().toString();
    }

    @ShellMethod("Get an advantage by its id (advantageById ADVANTAGE_ID)")
    public CliAdvantage advantageById(Long advantageId) {
        CliAdvantage cliAdvantage = restTemplate.getForObject(BASE_URI + "/" + advantageId, CliAdvantage.class);
        return cliAdvantage;
    }

    @ShellMethod("Get all advantages of a society (advantagesBySociety SOCIETY_ID)")
    public List<CliAdvantage> advantagesBySociety(Long societyId) {
        CliSociety cliSociety = societyCommands.societyById(societyId);
        List<CliAdvantage> cliAdvantages = restTemplate.getForObject(BASE_URI + "/society/" + cliSociety.getId(), List.class);
        return cliAdvantages;
    }

    @ShellMethod("Get all advantages elligible")
    public List<CliAdvantage> getAdvantagesElligible(String emailConsumer) {
        CliConsumer cliConsumer = cliContext.getConsumers().get(emailConsumer);
        Long idConsumer = cliConsumer.getId();
        List<CliAdvantage> cliAdvantages = restTemplate.getForObject(BASE_URI + "/consumer/" + idConsumer, List.class);
        return cliAdvantages;
    }

    @ShellMethod("Get all advantages elligible")
    public List<CliSelectedAdvantageOut> getSelectedAdvantagesElligible(String emailConsumer) {
        CliConsumer cliConsumer = cliContext.getConsumers().get(emailConsumer);
        Long idConsumer = cliConsumer.getId();
        List<CliSelectedAdvantageOut> cliSelectedAdvantages = restTemplate.getForObject(BASE_URI + "/selected/consumer/" + idConsumer, List.class);
        return cliSelectedAdvantages;
    }

    @ShellMethod("Select an advantage (selectAdvantage ADVANTAGE_NAME CONSUMER_EMAIL)")
    public CliSelectedAdvantageOut selectAdvantage(String nameAdvantage, String emailConsumer) {
        CliConsumer cliConsumer = cliContext.getConsumers().get(emailConsumer);
        Long idConsumer = cliConsumer.getId();
        Long idAdvantage = cliContext.getAdvantages().get(nameAdvantage).getId();
        CliSelectedAdvantageOut cliSelectedAdvantage = restTemplate.postForObject(BASE_URI + "/" + idConsumer + "/select-advantage", idAdvantage, CliSelectedAdvantageOut.class);
        cliContext.getSelectedAdvantages().put(cliSelectedAdvantage.getId().toString(), cliSelectedAdvantage);
        return cliSelectedAdvantage;
    }

    @ShellMethod("Get an selected advantage by its id (selectedAdvantageId SELECTED_ADVANTAGE_ID)")
    public CliSelectedAdvantageOut selectedAdvantageById(Long selectedAdvantageId) {
        CliSelectedAdvantageOut cliSelectedAdvantageOut = restTemplate.getForObject(BASE_URI + "/selected/" + selectedAdvantageId, CliSelectedAdvantageOut.class);
        return cliSelectedAdvantageOut;
    }

    @ShellMethod("Use an advantage (useAdvantage ADVANTAGE_NAME EMAIL_CONSUMER)")
    public CliSelectedAdvantageOut useAdvantage(String advantageName, String emailConsumer) {
        Map<String, CliSelectedAdvantageOut> a = cliContext.getSelectedAdvantages();
        Long idAdvantage = cliContext.getAdvantages().get(advantageName).getId();
        CliSelectedAdvantageOut cliSelectedAdvantage = null;
        for( Map.Entry<String, CliSelectedAdvantageOut> b : a.entrySet()){
            if(Objects.equals(b.getValue().getAdvantageId(), idAdvantage)){
                 cliSelectedAdvantage = b.getValue();
            }
        }
        restTemplate.put(BASE_URI+"/use-advantage", cliSelectedAdvantage.getId(), CliSelectedAdvantageOut.class);
        cliSelectedAdvantage = selectedAdvantageById(cliSelectedAdvantage.getId());
        cliContext.getSelectedAdvantages().put(cliSelectedAdvantage.getId().toString(), cliSelectedAdvantage);
        return cliSelectedAdvantage;
    }

    @ShellMethod("get an selected advantage (useAdvantage ADVANTAGE_NAME EMAIL_CONSUMER)")
    public CliSelectedAdvantageOut getSelectedAdvantage(String advantageName, String emailConsumer) {
        Map<String, CliSelectedAdvantageOut> a = cliContext.getSelectedAdvantages();
        Long idAdvantage = cliContext.getAdvantages().get(advantageName).getId();
        CliSelectedAdvantageOut cliSelectedAdvantage = null;
        for( Map.Entry<String, CliSelectedAdvantageOut> b : a.entrySet()){
            if(Objects.equals(b.getValue().getAdvantageId(), idAdvantage)){
                cliSelectedAdvantage = b.getValue();
            }
        }
        cliSelectedAdvantage = selectedAdvantageById(cliSelectedAdvantage.getId());
        cliContext.getSelectedAdvantages().put(cliSelectedAdvantage.getId().toString(), cliSelectedAdvantage);
        return cliSelectedAdvantage;
    }
}
