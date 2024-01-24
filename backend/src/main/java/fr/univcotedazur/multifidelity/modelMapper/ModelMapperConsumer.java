package fr.univcotedazur.multifidelity.modelMapper;


import fr.univcotedazur.multifidelity.controllers.dto_in.ConsumerDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.ConsumerDtoOut;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface ModelMapperConsumer {

    @Mapping(target = "favSocieties",ignore = true)
    @Mapping(target = "card",ignore = true)
    Consumer convertDtoInToConsumer(ConsumerDtoIn consumerDtoIn);

    @Mapping(source = "card.id", target = "cardId")
    @Mapping(target = "favSocietyIds",ignore = true)
    ConsumerDtoOut convertConsumerToDto(Consumer consumer);

    @AfterMapping
    default void convertConsumerToDtoAfterMapping(Consumer consumer, @MappingTarget ConsumerDtoOut consumerDtoOut){
        consumerDtoOut.setFavSocietyIds(consumer.getFavSocieties().stream().map(Society::getId)
                .collect(Collectors.toList()));
    }
}
