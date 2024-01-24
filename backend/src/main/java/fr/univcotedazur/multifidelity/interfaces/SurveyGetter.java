package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

import java.util.List;

public interface SurveyGetter {

    Survey getSurveyById(Long id) throws ResourceNotFoundException;

    List<Survey> getAllSurvey();
}
