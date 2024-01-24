package fr.univcotedazur.multifidelity.modelMapper;

import fr.univcotedazur.multifidelity.controllers.dto_in.SurveyAnswerDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.SurveyAnswerDtoOut;
import fr.univcotedazur.multifidelity.entities.SurveyAnswer;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ModelMapperSurveyAnswer {


    @Mapping(target = "consumer",ignore = true)
    SurveyAnswer convertDtoInToSurveyAnswer (SurveyAnswerDtoIn surveyDtoIn, @Context ConsumerGetter consumerGetter);

    @AfterMapping
    default void convertDtoInToSurveyAnswerAfterMapping(SurveyAnswerDtoIn surveyAnswerDtoIn, @MappingTarget SurveyAnswer surveyAnswer, @Context ConsumerGetter consumerGetter){
        Optional.ofNullable(surveyAnswerDtoIn.getConsumerId()).ifPresent(consumerId -> surveyAnswer.setConsumer(consumerGetter.getConsumerById(consumerId)));
    }

    @Mapping(source = "survey.id", target = "surveyId")
    @Mapping(source = "consumer.id", target = "consumerId")
    SurveyAnswerDtoOut convertSurveyAnswerToDtoOut(SurveyAnswer surveyAnswer);
}
