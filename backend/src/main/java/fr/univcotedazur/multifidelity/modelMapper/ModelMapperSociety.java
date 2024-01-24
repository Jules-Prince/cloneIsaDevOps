package fr.univcotedazur.multifidelity.modelMapper;

import fr.univcotedazur.multifidelity.controllers.dto_in.SocietyDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.SocietyDtoOut;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.interfaces.PartnerGetter;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ModelMapperSociety {



    @Mapping(target = "partner",ignore = true)
     Society convertDtoInToSociety(SocietyDtoIn societyDtoIn,@Context PartnerGetter partnerGetter);

    @AfterMapping
    default void convertDtoInToSocietyAfterMapping(SocietyDtoIn societyDtoIn, @MappingTarget Society society, @Context PartnerGetter partnerGetter){
        Optional.ofNullable(societyDtoIn.getPartnerId()).ifPresent(partnerId -> society.setPartner(partnerGetter.getPartnerById(partnerId)));
    }

    @Mapping(source = "partner.id", target = "partnerId")
    SocietyDtoOut convertSocietyToDtoOut(Society society) ;
}
