package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Survey;

public interface SurveyAnswerAnalyst {

    float getSatisfactionBySurvey(Survey survey);
}
