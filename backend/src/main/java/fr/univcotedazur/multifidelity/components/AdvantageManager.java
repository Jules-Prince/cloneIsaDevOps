package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.Validity;
import fr.univcotedazur.multifidelity.interfaces.AdvantageFinder;
import fr.univcotedazur.multifidelity.interfaces.AdvantageModifier;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.ValidityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AdvantageManager implements AdvantageModifier, AdvantageFinder{

    @Autowired
    private  AdvantageRepository advantageRepository;

    @Autowired
    private ValidityRepository validityRepository;


    @Override
    public Advantage addAdvantage(Advantage advantage) {
        if(advantage.getValidity()!=null){
            Validity validity =validityRepository.save(advantage.getValidity());
            advantage.setValidity(validity);
        }
        return advantageRepository.save(advantage);
    }

    @Override
    public void deleteAdvantage(Advantage advantage) {
       advantageRepository.deleteById(advantage.getId());
    }

    @Override
    public Advantage updateAdvantage(Advantage oldAdvantage, Advantage newAdvantage) {
        oldAdvantage.setName(newAdvantage.getName());
        oldAdvantage.setPoint(newAdvantage.getPoint());
        oldAdvantage.setPrice(newAdvantage.getPrice());
        oldAdvantage.setInitialPrice(newAdvantage.getInitialPrice());
        oldAdvantage.setVfpAdvantage(newAdvantage.isVfpAdvantage());
        oldAdvantage.setValidity(newAdvantage.getValidity());
        return oldAdvantage;
    }



    @Override
    @Transactional(readOnly = true)
    public Optional<Advantage> findAdvantageById(Long id) {
        return advantageRepository.findById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Advantage> getAllAdvantages() {
        return new ArrayList<>(advantageRepository.findAll());
    }



    @Override
    @Transactional(readOnly = true)
    public List<Advantage> getAllAdvantageBySociety(Society society) {
        return advantageRepository.findAll().stream().filter(advantage -> advantage.getSociety()==society).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Advantage> findAdvantageByNameBySociety(String name, Society society) {
        return getAllAdvantageBySociety(society).stream().filter(advantage -> Objects.equals(advantage.getName(), name)).findFirst();
    }
}