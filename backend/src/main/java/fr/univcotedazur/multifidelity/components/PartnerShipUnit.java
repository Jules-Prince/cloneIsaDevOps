package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.exceptions.AccountNotFoundException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistingAccountException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.PartnerAccountManager;
import fr.univcotedazur.multifidelity.interfaces.PartnerGetter;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
@Transactional
public class PartnerShipUnit implements PartnerAccountManager, PartnerGetter {


    @Autowired
    private PartnerRepository partnerRepository;


    private Optional<Partner> findByIdPartner(Long id) {
        return partnerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Partner getPartnerById(Long id) throws ResourceNotFoundException {
        return findByIdPartner(id).orElseThrow(() -> new ResourceNotFoundException(String.format("No partner found for id: %s", id.toString())));
    }

    @Override
    @Transactional(readOnly = true)
    public Partner getPartnerByName(String name) throws ResourceNotFoundException {
        return findPartnerByName(name).orElseThrow(() -> new ResourceNotFoundException(String.format("No partner found for name: %s", name)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partner> getAllPartners() {
        return partnerRepository.findAll().stream().toList();
    }

    @Override
    public Partner login(String email, String password) throws AccountNotFoundException {
        return partnerRepository.findAll().stream()
                .filter(partner -> partner.getEmail().equals(email) && partner.getPassword().equals(password))
                .findFirst()
                .orElseThrow(()-> new AccountNotFoundException("No partner account was found with given informations"));
    }


    @Override
    public Partner addPartnerAccount(Partner newPartner) throws AlreadyExistingAccountException, AlreadyExistSimilarRessourceException {
        if (findPartnerByEmail(newPartner.getEmail()).isPresent()) {
            throw new AlreadyExistingAccountException(String.format("Partner with this email address already exists : %s",newPartner.getEmail()));
        }
        if(findPartnerByName(newPartner.getName()).isPresent()){
            throw new AlreadyExistSimilarRessourceException(String.format("Partner already exists with this name : %s",newPartner.getName()));
        }
        return partnerRepository.save(newPartner);
    }



    private Optional<Partner> findPartnerByName(String name) {
        return StreamSupport.stream(partnerRepository.findAll().spliterator(), false)
                .filter(partner -> name.equals(partner.getName())).findAny();
    }

    private Optional<Partner> findPartnerByEmail(String email) {
        return StreamSupport.stream(partnerRepository.findAll().spliterator(), false)
                .filter(partner -> partner.getEmail().equals(email)).findAny();
    }


}
