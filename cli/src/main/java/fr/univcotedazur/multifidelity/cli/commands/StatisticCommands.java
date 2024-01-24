package fr.univcotedazur.multifidelity.cli.commands;

import fr.univcotedazur.multifidelity.cli.CliContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class StatisticCommands {
    public static final String BASE_URI = "/statistics";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @Autowired
    SocietyCommands societyCommands;

    public StatisticCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Get profit of a society (get-profit-of-society SOCIETY_NAME)")
    public float getProfitOfSociety(String societyName) {
        Long societyId = cliContext.getSocieties().get(societyName).getId();
        return restTemplate.getForObject(BASE_URI + "/profits/society/" + societyId, float.class);
    }

    @ShellMethod("Get profit (get-profit)")
    public float getProfit() {
        return restTemplate.getForObject(BASE_URI + "/profits", float.class);
    }

    @ShellMethod("Get frequency of a society (get-frequency-of-society SOCIETY_NAME)")
    public float getFrequencyOfSociety(String societyName) {
        Long societyId = cliContext.getSocieties().get(societyName).getId();
        return restTemplate.getForObject(BASE_URI + "/frequency-purchase/societies/" + societyId, float.class);
    }

    @ShellMethod("Get frequency (get-frequency)")
    public float getFrequency() {
        return restTemplate.getForObject(BASE_URI + "/frequency-purchase", float.class);
    }
}
