package fr.univcotedazur.multifidelity.modelMapper;


import fr.univcotedazur.multifidelity.controllers.dto_in.AdvantageDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.AdvantageDtoOut;
import fr.univcotedazur.multifidelity.controllers.dto_out.ValidityDtoOut;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Validity;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ModelMapperAdvantage {

    @Mapping(source = "society.id", target = "societyId")
    @Mapping(target = "validity",ignore = true)
    AdvantageDtoOut convertAdvantageToDtoOut(Advantage advantage);

    @AfterMapping
    default void convertAdvantageToDtoOutAfterMapping(Advantage advantage, @MappingTarget AdvantageDtoOut advantageDtoOut){
        Validity validity = advantage.getValidity();
        if(validity!=null){
            advantageDtoOut.setValidity(new ValidityDtoOut(validity.getNumberOfUse(),validity.getDurationPerUseBetweenUse(),validity.getDuration(),validity.isParc()));
        }
     }


    @Mapping(target = "society",ignore = true)
    @Mapping(target = "validity",ignore = true)
    Advantage convertDtoInToAdvantage(AdvantageDtoIn advantageDtoIn,@Context SocietyGetter societyGetter);


    @AfterMapping
    default void convertDtoInToAdvantageAfterMapping(AdvantageDtoIn advantageDtoIn, @MappingTarget Advantage advantage, @Context SocietyGetter societyGetter){
        ValidityDtoOut validityDtoOut = advantageDtoIn.getValidity();
        if(validityDtoOut!=null){
            advantage.setValidity(new Validity(validityDtoOut.getNumberOfUse(),validityDtoOut.getDurationPerUseBetweenUse(),validityDtoOut.getDuration(),validityDtoOut.isParc()));
        }
        Optional.ofNullable(advantageDtoIn.getSocietyId()).ifPresent(societyId -> advantage.setSociety(societyGetter.getSocietyById(societyId)));
    }

}
