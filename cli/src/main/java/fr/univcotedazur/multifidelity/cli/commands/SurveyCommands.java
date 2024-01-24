package fr.univcotedazur.multifidelity.cli.commands;

import fr.univcotedazur.multifidelity.cli.CliContext;
import fr.univcotedazur.multifidelity.cli.model.CliSatisfactionType;
import fr.univcotedazur.multifidelity.cli.model.CliSurvey;
import fr.univcotedazur.multifidelity.cli.model.CliSurveyAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ShellComponent
public class SurveyCommands {

    public static final String BASE_URI = "/survey";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @Autowired
    public SurveyCommands(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ShellMethod("Create a survey in the backend(createSurvey SURVEY_NAME SURVEY_DESCRIPTION --questions=SURVEY_QUESTIONS)")
    public CliSurvey createSurvey(String name, String description, String[] questions){
        List<String> questionList = new ArrayList<>();
        questionList.addAll(Arrays.asList(questions));
        CliSurvey cliSurvey = new CliSurvey(name, description, questionList);
        cliSurvey = restTemplate.postForObject(BASE_URI, cliSurvey, CliSurvey.class);
        cliContext.getSurveys().put(name, cliSurvey);
        return cliSurvey;
    }

    @ShellMethod("Get a survey in the backend(getSurvey SURVEY_NAME)")
    public CliSurvey getSurveyById(Long surveyId){
        CliSurvey cliSurvey = restTemplate.getForObject(BASE_URI + "/" + surveyId, CliSurvey.class);
        cliContext.getSurveys().put(cliSurvey.getName(), cliSurvey);
        return cliSurvey;
    }

     @ShellMethod("Get satisfaction rate of a survey in the backend(get-satisfaction SURVEY_NAME)")
    public Float getSatisfaction(String surveyName){
        CliSurvey cliSurvey = cliContext.getSurveys().get(surveyName);
        return restTemplate.getForObject(BASE_URI + "/" + cliSurvey.getId() + "/satisfaction", Float.class);
    }

    @ShellMethod("Add answer to a survey in the backend(add-answer SURVEY_NAME CONSUMER_EMAIL ANSWER)")
    public CliSurveyAnswer addAnswer(String surveyName, String consumerEmail, String[] answers){
        Long surveyId = cliContext.getSurveys().get(surveyName).getId();
        Long consumerId = cliContext.getConsumers().get(consumerEmail).getId();
        List<CliSatisfactionType> answersCliSatisfactionType = new ArrayList<>();
        for(String answer : answers){
            answersCliSatisfactionType.add(CliSatisfactionType.valueOf(answer));
        }
        CliSurveyAnswer cliSurveyAnswer = new CliSurveyAnswer(consumerId , surveyId, answersCliSatisfactionType);
        cliSurveyAnswer = restTemplate.postForObject(BASE_URI + "/" + surveyId + "/answer", cliSurveyAnswer, CliSurveyAnswer.class);
        cliContext.getSurveyAnswers().put(consumerEmail, cliSurveyAnswer);
        return cliSurveyAnswer;
    }
}
