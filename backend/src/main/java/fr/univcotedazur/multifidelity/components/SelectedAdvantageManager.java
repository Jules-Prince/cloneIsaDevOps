package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;
import fr.univcotedazur.multifidelity.interfaces.AdvantageSelectedModifier;
import fr.univcotedazur.multifidelity.interfaces.SelectedAdvantageFinder;
import fr.univcotedazur.multifidelity.repositories.SelectedAdvantageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SelectedAdvantageManager implements SelectedAdvantageFinder, AdvantageSelectedModifier {


    @Autowired
    private SelectedAdvantageRepository selectedAdvantageRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<SelectedAdvantage> findSelectedAdvantageById(Long id) {
        return selectedAdvantageRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SelectedAdvantage> getAllSelectedAdvantages() {
        return new ArrayList<>(selectedAdvantageRepository.findAll());
    }

    @Override
    public SelectedAdvantage addSelectAdvantage(Advantage advantage, Card card) {
        SelectedAdvantage newSelectedAdvantage = new SelectedAdvantage();
        LocalDateTime now = LocalDateTime.now();
        newSelectedAdvantage.setState(StateSelectedAdvantage.AVAILABLE);
        newSelectedAdvantage.setAdvantage(advantage);
        newSelectedAdvantage.setCard(card);
        newSelectedAdvantage.setNumberOfUse(0);
        newSelectedAdvantage.setCreation(now);
        newSelectedAdvantage.setLastUse(null);
        return selectedAdvantageRepository.save(newSelectedAdvantage);

    }

    @Override
    public SelectedAdvantage setState(SelectedAdvantage selectedAdvantage,StateSelectedAdvantage stateSelectedAdvantage) {
        selectedAdvantage.setState(stateSelectedAdvantage);
        return selectedAdvantage;
    }

    @Override
    public SelectedAdvantage setLastUse(LocalDateTime lastUse,SelectedAdvantage selectedAdvantage) {
        selectedAdvantage.setLastUse(lastUse);
        return selectedAdvantage;
    }

    @Override
    public SelectedAdvantage incrementNbUse(SelectedAdvantage selectedAdvantage, Duration durationPerUseBetweenUse) {
        LocalDateTime lastUse = selectedAdvantage.getLastUse();
        if(lastUse == null || lastUse.isBefore(LocalDateTime.now().minus(durationPerUseBetweenUse))){ // si tu l'a pas utlisé dans la journée ou si tu l'a jamais utilisé
            selectedAdvantage.setNumberOfUse(0);
        }
        selectedAdvantage.setNumberOfUse(selectedAdvantage.getNumberOfUse()+1);
        return selectedAdvantage;
    }
}
