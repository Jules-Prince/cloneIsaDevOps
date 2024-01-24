package fr.univcotedazur.multifidelity.modelMapper;

import fr.univcotedazur.multifidelity.controllers.dto_in.SurveyDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.SurveyDtoOut;
import fr.univcotedazur.multifidelity.entities.Survey;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelMapperSurvey {

     Survey convertDtoInToSurvey(SurveyDtoIn surveyDtoIn);


    SurveyDtoOut convertSurveyToDtoOut(Survey survey) ;
}
