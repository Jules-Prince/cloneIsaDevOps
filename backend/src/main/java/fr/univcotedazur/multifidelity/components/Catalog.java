package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.NotAvailableAdvantageException;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughPointException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.AdvantageFinder;
import fr.univcotedazur.multifidelity.interfaces.AdvantageGetter;
import fr.univcotedazur.multifidelity.interfaces.AdvantageModifier;
import fr.univcotedazur.multifidelity.interfaces.CatalogModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(noRollbackFor = {NotAvailableAdvantageException.class, NotEnoughPointException.class})
public class Catalog implements CatalogModifier, AdvantageGetter {

    @Autowired
    private AdvantageModifier advantageModifier;

    @Autowired
    private AdvantageFinder advantageFinder;


    @Override
    public Advantage createAdvantage(Advantage advantage) throws AlreadyExistSimilarRessourceException {
        if(advantageFinder.findAdvantageByNameBySociety(advantage.getName(),advantage.getSociety()).isPresent()){
            throw new AlreadyExistSimilarRessourceException("a similar advantage already exists in this society");
        }
        return advantageModifier.addAdvantage(advantage);
    }

    @Override
    public void deleteAdvantage(Advantage advantage) {
        advantageModifier.deleteAdvantage(advantage);
    }

    @Override
    public Advantage updateAdvantage(Advantage oldAdvantage, Advantage newAdvantage) {
        return advantageModifier.updateAdvantage(oldAdvantage,newAdvantage);
    }



    @Override
    @Transactional(readOnly = true)
    public Advantage getAdvantageById(Long id) throws ResourceNotFoundException {
        return advantageFinder.findAdvantageById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("No advantage found for id: %s", id.toString())));
    }

    @Override
    @Cacheable("advantages")
    @Transactional(readOnly = true)
    public List<Advantage> getAllAdvantages() {
        return advantageFinder.getAllAdvantages();
    }



    @Override
    public List<Advantage> getAllAdvantagesBySociety(Society society) {
        return  advantageFinder.getAllAdvantageBySociety(society);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Advantage> getAllAdvantageEligibleByConsumer(Consumer consumer) {
        List<Advantage> advantagesEligibleByPoint = getAllAdvantages().stream().filter(advantage -> advantage.getPoint()<=consumer.getCard().getPoint()).collect(Collectors.toList());
        if(!consumer.isVfp()){
            return advantagesEligibleByPoint.stream().filter(advantage -> !advantage.isVfpAdvantage()).collect(Collectors.toList());
        }
        return advantagesEligibleByPoint;
    }






}
