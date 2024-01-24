package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.SurveyCreator;
import fr.univcotedazur.multifidelity.interfaces.SurveyFinder;
import fr.univcotedazur.multifidelity.interfaces.SurveyGetter;
import fr.univcotedazur.multifidelity.interfaces.SurveyModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class SurveyHandler implements SurveyGetter, SurveyCreator {

    @Autowired
    private SurveyFinder surveyFinder;

    @Autowired
    private SurveyModifier surveyModifier;

    @Override
    public Survey createSurvey(Survey survey) {
        return surveyModifier.addSurvey(survey);
    }


    @Override
    public Survey getSurveyById(Long id) throws ResourceNotFoundException {
        return surveyFinder.findSurveyById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Survey with id %s not found", id)));
    }

    @Override
    public List<Survey> getAllSurvey() {
        return surveyFinder.getAllSurvey();
    }


}
