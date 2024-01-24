package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;

import java.time.Duration;
import java.time.LocalDateTime;

public interface AdvantageSelectedModifier {

    SelectedAdvantage addSelectAdvantage(Advantage advantage, Card card);


    SelectedAdvantage setState(SelectedAdvantage selectedAdvantage,StateSelectedAdvantage stateSelectedAdvantage);


    SelectedAdvantage setLastUse(LocalDateTime lastUse,SelectedAdvantage selectedAdvantage);


    SelectedAdvantage incrementNbUse(SelectedAdvantage selectedAdvantage, Duration durationPerUseBetweenUse);
}

