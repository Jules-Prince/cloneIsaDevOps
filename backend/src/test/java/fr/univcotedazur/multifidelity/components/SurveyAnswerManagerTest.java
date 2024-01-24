package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.SatisfactionType;
import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.entities.SurveyAnswer;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.SurveyAnswerRepository;
import fr.univcotedazur.multifidelity.repositories.SurveyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Commit
public class SurveyAnswerManagerTest {

    @Autowired
    private SurveyManager surveyManager;

    @Autowired
    private SurveyAnswerManager surveyAnswerManager;

    @Autowired
    private SurveyRepository surveyRepository;


    @Autowired
    private ConsumerRepository consumerRepository;



    @Autowired
    private SurveyAnswerRepository surveyAnswerRepository;

    Survey survey;

    Consumer consumer;

    @BeforeEach
    public void setUpContext() {
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        survey = new Survey( "survey1", "description1",questions);
        consumer = new Consumer("djulo.fr@bg.com", "fleur1234", "123456789", "YOOO", null);
        consumer =consumerRepository.save(consumer);
    }

    @AfterEach
    public void cleanUpContext() throws Exception{
        surveyAnswerRepository.deleteAll();
        surveyRepository.deleteAll();
        consumerRepository.deleteAll();
    }

    @Test
    public void getSatisfactionBySurveyAnswer(){
        surveyManager.addSurvey(survey);
        SurveyAnswer surveyAnswer =new SurveyAnswer(consumer,survey,List.of(SatisfactionType.SATISFIED));
        assertEquals(surveyAnswerManager.getSatisfactionBySurveyAnswer(surveyAnswer), 4);
    }

    @Test
    public void getSatisfactionByQuestionTest2(){
        surveyManager.addSurvey(survey);
        SurveyAnswer surveyAnswer =new SurveyAnswer(consumer,survey,List.of(SatisfactionType.SATISFIED,SatisfactionType.UNSATISFIED));
        assertEquals(surveyAnswerManager.getSatisfactionBySurveyAnswer(surveyAnswer),3 );

    }

    @Test
    public void getSatisfactionBySurveyTest(){
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        questions.add("question2");
        Survey survey3 = new Survey( "survey3", "description3",questions);
        surveyManager.addSurvey(survey3);
        SurveyAnswer surveyAnswer =new SurveyAnswer(consumer,survey3,List.of(SatisfactionType.SATISFIED,SatisfactionType.UNSATISFIED));
        surveyAnswerManager.addAnswerToSurvey(surveyAnswer);
        SurveyAnswer surveyAnswer2 =new SurveyAnswer(consumer,survey3,List.of(SatisfactionType.VERY_UNSATISFIED,SatisfactionType.VERY_SATISFIED));
        surveyAnswerManager.addAnswerToSurvey(surveyAnswer2);
        assertEquals(surveyAnswerManager.getSatisfactionBySurvey(survey3), 3);
    }

    @Test
    void addAnswerToSurveyTest() {
        Survey survey1 =surveyManager.addSurvey(survey);
        SurveyAnswer surveyAnswer =surveyAnswerManager.addAnswerToSurvey(new SurveyAnswer(consumer,survey,List.of(SatisfactionType.NEUTRAL)));
        assertEquals(1, surveyRepository.count());
        assertEquals(surveyAnswer.getSurvey(), survey1);
    }

}
