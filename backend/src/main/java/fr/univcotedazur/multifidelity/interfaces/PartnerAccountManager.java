package fr.univcotedazur.multifidelity.interfaces;


import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.exceptions.AccountNotFoundException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistingAccountException;

public interface PartnerAccountManager {

    Partner addPartnerAccount(Partner newPartner) throws AlreadyExistingAccountException, AlreadyExistSimilarRessourceException;

    Partner login(String email , String password) throws AccountNotFoundException;
}
