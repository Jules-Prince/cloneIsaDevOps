package fr.univcotedazur.multifidelity.cli.commands;

import fr.univcotedazur.multifidelity.cli.CliContext;
import fr.univcotedazur.multifidelity.cli.model.CliConsumer;
import fr.univcotedazur.multifidelity.cli.model.CliSociety;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class FavSocietiesCommands {
    public static final String BASE_URI = "/fav_societies";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @Autowired
    public FavSocietiesCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Add a society to the list of favorite societies of a consumer (add-fav-society EMAIL_CONSUMER NAME_SOCIETY)")
    public CliConsumer addFavSociety(String emailConsumer, String  nameSociety) {
        CliConsumer cliConsumer = cliContext.getConsumers().get(emailConsumer);
        Long idConsumer = cliConsumer.getId();
        Long idSociety = cliContext.getSocieties().get(nameSociety).getId();
        cliConsumer = restTemplate.postForObject(BASE_URI + "/" + idConsumer , idSociety, CliConsumer.class);
        cliContext.getConsumers().put(cliConsumer.getEmail(), cliConsumer);
        return cliConsumer;
    }

    @ShellMethod("Remove a society from the list of favorite societies of a consumer (delete-fav-society ID_CONSUMER ID_SOCIETY)")
    public void deleteFavSociety(String emailConsumer, String  nameSociety) {
        CliConsumer cliConsumer = cliContext.getConsumers().get(emailConsumer);
        Long idConsumer = cliConsumer.getId();
        Long idSociety = cliContext.getSocieties().get(nameSociety).getId();
        restTemplate.delete(BASE_URI + "/" + idConsumer, idSociety);
        cliContext.getConsumers().put(cliConsumer.getEmail(), cliConsumer);
    }

    @ShellMethod("Get the list of favorite societies of a consumer (get-fav-societies EMAIL_CONSUMER)")
    public String getFavSocieties(String emailConsumer) {
        CliConsumer cliConsumer = cliContext.getConsumers().get(emailConsumer);
        Long idConsumer = cliConsumer.getId();
        List<CliSociety> favSocieties = restTemplate.getForObject(BASE_URI + "/" + idConsumer, List.class, idConsumer);
        return Optional.ofNullable(favSocieties).map(String::valueOf).orElse("No advantages available");
    }
}
