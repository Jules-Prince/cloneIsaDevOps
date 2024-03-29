package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

import java.util.List;

public interface PartnerGetter {
    Partner getPartnerById(Long id) throws ResourceNotFoundException;

    Partner getPartnerByName(String name) throws ResourceNotFoundException;

    List<Partner> getAllPartners();
}
