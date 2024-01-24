package fr.univcotedazur.multifidelity.modelMapper;

import fr.univcotedazur.multifidelity.controllers.dto_out.SelectedAdvantageDtoOut;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ModelMapperSelectedAdvantage {

    @Mapping(source = "card.id", target = "cardId")
    @Mapping(source = "advantage.id", target = "advantageId")
    SelectedAdvantageDtoOut convertSelectedAdvantageToDto(SelectedAdvantage selectedAdvantage);


}
