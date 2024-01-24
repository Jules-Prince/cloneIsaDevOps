package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.entities.SurveyAnswer;
import fr.univcotedazur.multifidelity.exceptions.InvalidAnswerSurveyException;
import fr.univcotedazur.multifidelity.interfaces.SatisfactionGetter;
import fr.univcotedazur.multifidelity.interfaces.SurveyAnswerAnalyst;
import fr.univcotedazur.multifidelity.interfaces.SurveyAnswerModifier;
import fr.univcotedazur.multifidelity.interfaces.SurveyAnswerer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SurveyAnswerHandler implements SatisfactionGetter, SurveyAnswerer {

    @Autowired
    private SurveyAnswerAnalyst surveyAnalyst;

    @Autowired
    private SurveyAnswerModifier surveyAnswerModifier;


    @Override
    public float getSatisfaction(Survey survey) {
        return surveyAnalyst.getSatisfactionBySurvey(survey);
    }

    @Override
    public SurveyAnswer addAnswerToSurvey(Survey survey , SurveyAnswer surveyAnswer)  {
        if(survey.getQuestions().size() != surveyAnswer.getAnswers().size()){
            throw new InvalidAnswerSurveyException(String.format("The answer to the survey %s is incorrect, some answers have no answers",survey.getName()));
        }
        surveyAnswer.setSurvey(survey);
        return surveyAnswerModifier.addAnswerToSurvey(surveyAnswer);
    }


}
