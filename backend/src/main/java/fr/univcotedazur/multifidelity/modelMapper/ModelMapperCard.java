package fr.univcotedazur.multifidelity.modelMapper;

import fr.univcotedazur.multifidelity.controllers.dto_out.CardDtoOut;
import fr.univcotedazur.multifidelity.entities.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ModelMapperCard {

    @Mapping(source = "consumer.id", target = "ownerId")
    CardDtoOut convertCardToDto(Card card);
}
