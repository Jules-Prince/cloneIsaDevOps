package fr.univcotedazur.multifidelity.modelMapper;

import fr.univcotedazur.multifidelity.controllers.dto_in.PartnerDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.PartnerDtoOut;
import fr.univcotedazur.multifidelity.entities.Partner;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ModelMapperPartner {

    PartnerDtoOut convertPartnerToDtoOut(Partner partner) ;


    Partner convertDtoInToPartner(PartnerDtoIn partnerDtoIn) ;

}
