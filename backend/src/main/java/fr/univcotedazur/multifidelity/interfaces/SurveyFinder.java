package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Survey;

import java.util.List;
import java.util.Optional;

public interface SurveyFinder {
    Optional<Survey> findSurveyById(Long id);

    List<Survey> getAllSurvey();

}
