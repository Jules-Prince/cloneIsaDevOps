package fr.univcotedazur.multifidelity.modelMapper;

import fr.univcotedazur.multifidelity.controllers.dto_in.ValidityDtoIn;
import fr.univcotedazur.multifidelity.entities.Validity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelMapperValidity {

    Validity convertDtoInToValidity(ValidityDtoIn validityDtoIn);

}
