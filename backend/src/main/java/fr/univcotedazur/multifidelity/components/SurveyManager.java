package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.interfaces.SurveyFinder;
import fr.univcotedazur.multifidelity.interfaces.SurveyModifier;
import fr.univcotedazur.multifidelity.repositories.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SurveyManager implements SurveyModifier, SurveyFinder {
    @Autowired
    private SurveyRepository surveyRepository;


    @Override
    @Transactional(readOnly = true)
    public Optional<Survey> findSurveyById(Long id) {
        return surveyRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Survey> getAllSurvey() {
        return new ArrayList<>(surveyRepository.findAll());
    }


    @Override
    public Survey addSurvey(Survey survey) {
        survey.setDate(LocalDate.now());
        return surveyRepository.save(survey);
    }
}
