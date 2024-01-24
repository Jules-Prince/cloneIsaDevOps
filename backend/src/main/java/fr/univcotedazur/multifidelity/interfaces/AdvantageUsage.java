package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.connectors.externaldto.ParcDto;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.exceptions.NotAvailableAdvantageException;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughPointException;

public interface AdvantageUsage {

    SelectedAdvantage selectAdvantage(Advantage advantage, Consumer consumer) throws NotEnoughPointException;

    SelectedAdvantage useAdvantage(SelectedAdvantage selectedAdvantage) throws NotAvailableAdvantageException;

    void terminateAdvantage(Consumer consumer, ParcDto parcDto) throws NotAvailableAdvantageException;

}
