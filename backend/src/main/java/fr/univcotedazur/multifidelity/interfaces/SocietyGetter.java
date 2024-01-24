package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;

import java.util.List;

public interface SocietyGetter {

    Society getSocietyById(Long id) throws ResourceNotFoundException;

    List<Society> getAllSocieties();

    Society getSocietyByName(String name) throws ResourceNotFoundException;

}
