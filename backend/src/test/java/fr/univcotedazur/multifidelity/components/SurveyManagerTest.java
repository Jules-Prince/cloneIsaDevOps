package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Survey;
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
public class SurveyManagerTest {

    @Autowired
    private SurveyManager surveyManager;

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
    public void addSurveyTest()  {
        surveyManager.addSurvey(survey);
        assertEquals(1,  surveyRepository.count());
    }

    @Test
    public void findSurveyByIDTest()  {
        Survey survey1 =surveyManager.addSurvey(survey);
        assertEquals(survey1,  surveyManager.findSurveyById(survey.getId()).get());
    }

    @Test
    public void getAllSurveysTest()  {
        surveyManager.addSurvey(survey);
        List<String> questions2 = new ArrayList<>();
        questions2.add("question1");
        Survey survey2 = new Survey( "survey2", "description2",questions2);
        surveyManager.addSurvey(survey2);
        assertEquals(2,  surveyManager.getAllSurvey().size());
    }

}
