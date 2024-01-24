package fr.univcotedazur.multifidelity.cucumber.survey;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.SatisfactionType;
import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.entities.SurveyAnswer;
import fr.univcotedazur.multifidelity.interfaces.NotificationSender;
import fr.univcotedazur.multifidelity.interfaces.SurveyAnswerAnalyst;
import fr.univcotedazur.multifidelity.interfaces.SurveyAnswerer;
import fr.univcotedazur.multifidelity.interfaces.SurveyCreator;
import fr.univcotedazur.multifidelity.interfaces.SurveyGetter;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import fr.univcotedazur.multifidelity.repositories.SurveyAnswerRepository;
import fr.univcotedazur.multifidelity.repositories.SurveyRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SurveyStepDefs {

    @Autowired
    SurveyRepository surveyRepository;


    @Autowired
    SurveyAnswerer surveyAnswerer;

    @Autowired
    ConsumerRepository consumerRepository;

    @Autowired
    PartnerRepository partnerRepository;


    @Autowired
    SurveyAnswerRepository surveyAnswerRepository;

    @Autowired
    SurveyCreator surveyCreator;

    @Autowired
    SurveyAnswerAnalyst surveyAnalyst;

    @Autowired
    NotificationSender notificationSender;

    @Autowired
    SurveyGetter surveyGetter;


    Long surveyId;
    Partner partner;
    Long paulId;


    @AfterEach
    public void cleanUpContext() throws Exception {
        surveyAnswerRepository.deleteAll();
        surveyRepository.deleteAll();
        consumerRepository.deleteAll();
        partnerRepository.deleteAll();
    }


    @Given("a partner")
    public void aPartner() {
        partner = new Partner("partner1", "partner1", "partner1");
        partner = partnerRepository.save(partner);
    }

    @And("a client logged in as {string}")
    public void aClientLoggedInAs(String arg0) {
        Consumer paul = new Consumer();
        paul.setEmail(arg0);
        paulId = consumerRepository.save(paul).getId();
    }

    @When("the partner create a survey called {string} with description {string} and {int} questions")
    public void thePartnerCreateASurveyCalledWithDescriptionAndQuestions(String arg0, String arg1, int arg2) {
        List<String> questions = new ArrayList<>();
        for (int i = 0; i < arg2; i++) {
            questions.add("question" + i);
        }
        Survey survey = new Survey(arg0, arg1, questions);
        surveyId = surveyCreator.createSurvey(survey).getId();
    }

    @And("he send it to the clients")
    public void heSendItToTheClients() {
        notificationSender.sendSurveyToConsumer(surveyRepository.findById(surveyId).get(), consumerRepository.findById(paulId).get());
    }

    @Then("the clients can see the survey")
    public void theClientsCanSeeTheSurvey() {
        surveyGetter.getAllSurvey().contains(surveyRepository.findById(surveyId).get());
    }

    @And("the client {string} answer the survey {string} with {string} for the question {int} and {string} for the question {int}")
    public void theClientAnswerTheSurveyWithNEUTRALForTheQuestionAndNEUTRALForTheQuestion(String arg0, String arg1, String arg2, int arg3, String arg4, int arg5) {
        Survey survey = surveyRepository.findById(surveyId).get();
        Consumer paul = consumerRepository.findById(paulId).get();
        surveyAnswerer.addAnswerToSurvey(survey, new SurveyAnswer(paul, List.of(SatisfactionType.UNSATISFIED, SatisfactionType.SATISFIED)));
    }

    @Then("the partner can analyse the survey and see that the satisfaction for the survey is {float}")
    public void thePartnerCanAnalyseTheSurveyAndSeeThatTheSatisfactionForTheSurveyIs(float arg0) {
        Survey survey = surveyRepository.findById(surveyId).get();
        assertEquals(surveyAnalyst.getSatisfactionBySurvey(survey), arg0);
    }
}
