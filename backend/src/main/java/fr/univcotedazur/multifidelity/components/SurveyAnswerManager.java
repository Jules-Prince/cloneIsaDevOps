package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.SatisfactionType;
import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.entities.SurveyAnswer;
import fr.univcotedazur.multifidelity.interfaces.SurveyAnswerAnalyst;
import fr.univcotedazur.multifidelity.interfaces.SurveyAnswerModifier;
import fr.univcotedazur.multifidelity.repositories.SurveyAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SurveyAnswerManager implements SurveyAnswerAnalyst, SurveyAnswerModifier {

    @Autowired
    private SurveyAnswerRepository surveyAnswerRepository;



    public List<SurveyAnswer> getAllSurveyAnswerBySurvey(Survey survey) {
        return surveyAnswerRepository.findAll().stream().filter(surveyAnswer -> surveyAnswer.getSurvey()==survey).collect(Collectors.toList());
    }

    public float getSatisfactionBySurveyAnswer(SurveyAnswer surveyAnswer) {
        float sum = 0;
        List<SatisfactionType> answers = surveyAnswer.getAnswers();
        for(SatisfactionType satisfaction : answers){
            sum += satisfaction.getPoint();
        }
        return sum / answers.size();
    }

    @Override
    @Transactional(readOnly = true)
    public float getSatisfactionBySurvey(Survey survey) {
        float sum = 0;
        List<SurveyAnswer> surveyAnswers =getAllSurveyAnswerBySurvey(survey);
        for(SurveyAnswer surveyAnswer : surveyAnswers){
            sum += getSatisfactionBySurveyAnswer(surveyAnswer);
        }

        return sum==0 ? 0:sum / surveyAnswers.size();
    }

    @Override
    public SurveyAnswer addAnswerToSurvey(SurveyAnswer surveyAnswer) {
        surveyAnswer.setDate(LocalDate.now());
        return surveyAnswerRepository.save(surveyAnswer);
    }
}
