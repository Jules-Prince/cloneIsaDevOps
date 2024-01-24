package fr.univcotedazur.multifidelity.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.controllers.dto_in.SurveyAnswerDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_in.SurveyDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.SurveyDtoOut;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.SatisfactionType;
import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.entities.SurveyAnswer;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.SurveyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SurveyIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    ConsumerRepository consumerRepository;

    @BeforeEach
    public void setUp() {
        surveyRepository.deleteAll();
        consumerRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        surveyRepository.deleteAll();
        consumerRepository.deleteAll();
    }

    @Test
    public void validCreate() throws Exception{
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        SurveyDtoIn surveyDtoIn = new SurveyDtoIn("survey", "description", questions);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.SurveyRoutes.BASE_SURVEY_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void getAll() throws Exception {
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        Survey survey = new Survey("survey", "description", questions);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SurveyRoutes.BASE_SURVEY_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getById() throws Exception {
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        Survey survey = new Survey("survey", "description", questions);
        surveyRepository.save(survey);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SurveyRoutes.BASE_SURVEY_ROUTE + "/" + survey.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundGetById() throws Exception {
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        Survey survey = new Survey("survey", "description", questions);
        surveyRepository.save(survey);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SurveyRoutes.BASE_SURVEY_ROUTE + "/" + survey.getId()+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getSatisfactionById() throws Exception {
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        Survey survey = new Survey("survey", "description", questions);
        surveyRepository.save(survey);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SurveyRoutes.BASE_SURVEY_ROUTE + "/" + survey.getId() +"/satisfaction")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundGetSatisfactionById() throws Exception {
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        Survey survey = new Survey("survey", "description", questions);
        surveyRepository.save(survey);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SurveyRoutes.BASE_SURVEY_ROUTE + "/" + survey.getId()+1 +"/satisfaction")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void validAddAnswers() throws Exception {
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        Survey survey = new Survey("survey", "description", questions);
        surveyRepository.save(survey);
        Consumer consumer = new Consumer("momo@gmail.com", "momo", "1111111111111111", "TE-112-ST", null);
        consumerRepository.save(consumer);
        List<SatisfactionType> answers = new ArrayList<>();
        answers.add(SatisfactionType.SATISFIED);
        SurveyAnswerDtoIn surveyAnswerDtoIn = new SurveyAnswerDtoIn(consumer.getId(), survey.getId(), answers);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.SurveyRoutes.BASE_SURVEY_ROUTE + "/" + survey.getId() +"/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyAnswerDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundValidAddAnswers() throws Exception {
        List<String> questions = new ArrayList<>();
        questions.add("question1");
        Survey survey = new Survey("survey", "description", questions);
        surveyRepository.save(survey);
        List<SatisfactionType> answers = new ArrayList<>();
        answers.add(SatisfactionType.SATISFIED);
        Consumer consumer = new Consumer("momo@gmail.com", "momo", "1111111111111111", "TE-112-ST", null);
        consumerRepository.save(consumer);
        SurveyAnswerDtoIn surveyAnswerDtoIn = new SurveyAnswerDtoIn(consumer.getId(), survey.getId(), answers);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.SurveyRoutes.BASE_SURVEY_ROUTE + "/" + survey.getId()+1 +"/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyAnswerDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
