package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.entities.SurveyAnswer;

public interface SurveyAnswerer {

    SurveyAnswer addAnswerToSurvey(Survey survey , SurveyAnswer surveyAnswer);
}
