package fr.univcotedazur.multifidelity.modelMapper;

import fr.univcotedazur.multifidelity.controllers.dto_in.PurchaseDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.PurchaseDtoOut;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.interfaces.CardGetter;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;


@Mapper(componentModel = "spring")
public interface ModelMapperPurchase {

    @Mapping(target = "card",ignore = true)
    @Mapping(target = "society",ignore = true)
    Purchase convertDtoInToPurchase(PurchaseDtoIn purchaseDtoIn, @Context SocietyGetter societyGetter,@Context CardGetter cardGetter);

    @AfterMapping
    default void convertDtoInToPurchaseAfterMapping(PurchaseDtoIn purchaseDtoIn, @MappingTarget Purchase purchase, @Context SocietyGetter societyGetter,@Context CardGetter cardGetter){
        Optional.ofNullable(purchaseDtoIn.getSocietyId()).ifPresent(societyId -> purchase.setSociety(societyGetter.getSocietyById(societyId)));
        Optional.ofNullable(purchaseDtoIn.getCardId()).ifPresent(cardId -> purchase.setCard(cardGetter.getCardById(cardId)));
    }

    @Mapping(source = "society.id", target = "societyId")
    @Mapping(source = "card.id", target = "cardId")
    PurchaseDtoOut convertPurchaseToDtoOut(Purchase purchase);
}