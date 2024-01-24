package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.SatisfactionType;
import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.entities.SurveyAnswer;
import fr.univcotedazur.multifidelity.exceptions.InvalidAnswerSurveyException;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.SurveyAnswerRepository;
import fr.univcotedazur.multifidelity.repositories.SurveyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Commit
public class SurveyAnswerHandlerTest {

    @Autowired
    private SurveyHandler surveyHandler;

    @Autowired
    private SurveyAnswerHandler surveyAnswerHandler;

    @Autowired
    private SurveyRepository surveyRepository;


    @Autowired
    private ConsumerRepository consumerRepository;


    @Autowired
    private SurveyAnswerRepository surveyAnswerRepository;

    Survey survey;

    Consumer consumer;

    @BeforeEach
    void setUp() {
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        survey = new Survey("survey1", "description1",questions);
        consumer = new Consumer();
        consumer =consumerRepository.save(consumer);
    }

    @AfterEach
    public void cleanUpContext() throws Exception{
        surveyAnswerRepository.deleteAll();
        surveyRepository.deleteAll();
        consumerRepository.deleteAll();
    }

    @Transactional
    @Test
    void addAnswerToSurveyTest() {
        Survey survey1 =surveyHandler.createSurvey(survey);
        surveyAnswerHandler.addAnswerToSurvey(survey1,new SurveyAnswer(consumer, List.of(SatisfactionType.NEUTRAL)));
        assertEquals(1, surveyRepository.count());
    }

    @Test
    @Transactional(propagation= Propagation.NEVER)
    void addAnswerToSurveyTestInvalidAnswerSurveyException() {
        Survey survey1 =surveyHandler.createSurvey(survey);
        Assertions.assertThrows(InvalidAnswerSurveyException.class, () ->surveyAnswerHandler.addAnswerToSurvey(survey1,new SurveyAnswer(consumer,List.of(SatisfactionType.NEUTRAL,SatisfactionType.SATISFIED))));
    }
}
