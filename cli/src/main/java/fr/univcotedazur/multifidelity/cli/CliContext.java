package fr.univcotedazur.multifidelity.cli;

import fr.univcotedazur.multifidelity.cli.model.CliAdvantage;
import fr.univcotedazur.multifidelity.cli.model.CliCard;
import fr.univcotedazur.multifidelity.cli.model.CliConsumer;
import fr.univcotedazur.multifidelity.cli.model.CliPartner;
import fr.univcotedazur.multifidelity.cli.model.CliPurchase;
import fr.univcotedazur.multifidelity.cli.model.CliSelectedAdvantageOut;
import fr.univcotedazur.multifidelity.cli.model.CliSociety;
import fr.univcotedazur.multifidelity.cli.model.CliSurvey;
import fr.univcotedazur.multifidelity.cli.model.CliSurveyAnswer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CliContext {

    private Map<String, CliAdvantage> advantages; //nameAdvantage, CliAdvantage
    private Map<String, CliCard> cards; //ownerEmail, CliCard
    private Map<String, CliConsumer> consumers; //email, CliConsumer
    private Map<String, CliPartner> partners; //email, CliPartner
    private Map<Long, CliPurchase> purchases; //id, CliPurchase
    private Map<String, CliSelectedAdvantageOut> selectedAdvantages; //advantage.name, CliSelectedAdvantage
    private Map<String, CliSociety> societies; //name, CliSociety
    private Map<String, CliSurvey> surveys; //name, CliSurvey
    private Map<String, CliSurveyAnswer> surveyAnswers; //consumerEmail, CliSurveyAnswer


    public Map<String, CliAdvantage> getAdvantages() {
        return advantages;
    }
    public Map<String, CliCard> getCards() {
        return cards;
    }
    public Map<String, CliConsumer> getConsumers() {
        return consumers;
    }
    public Map<String, CliPartner> getPartners() {
        return partners;
    }
    public Map<Long, CliPurchase> getPurchases() {
        return purchases;
    }
    public Map<String, CliSelectedAdvantageOut> getSelectedAdvantages() {
        return selectedAdvantages;
    }
    public Map<String, CliSociety> getSocieties() {
        return societies;
    }
    public Map<String, CliSurvey> getSurveys() {
        return surveys;
    }
    public Map<String, CliSurveyAnswer> getSurveyAnswers() {
        return surveyAnswers;
    }


    public CliContext() {
        advantages = new HashMap<>();
        cards = new HashMap<>();
        consumers = new HashMap<>();
        partners = new HashMap<>();
        purchases = new HashMap<>();
        selectedAdvantages = new HashMap<>();
        societies = new HashMap<>();
        surveys = new HashMap<>();
        surveyAnswers = new HashMap<>();
    }

    /*
    @Override
    public String toString() {
        return consumers.keySet().stream()
                .map(key -> key + "=" + consumers.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }*/

}
