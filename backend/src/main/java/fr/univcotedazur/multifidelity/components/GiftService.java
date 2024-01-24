package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.connectors.externaldto.ParcDto;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Validity;
import fr.univcotedazur.multifidelity.exceptions.IWYPLSConnexionException;
import fr.univcotedazur.multifidelity.exceptions.NotAvailableAdvantageException;
import fr.univcotedazur.multifidelity.exceptions.NotEnoughPointException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.AdvantageSelectedModifier;
import fr.univcotedazur.multifidelity.interfaces.AdvantageUsage;
import fr.univcotedazur.multifidelity.interfaces.IsawWhereYouParkedLastSummer;
import fr.univcotedazur.multifidelity.interfaces.NotificationSender;
import fr.univcotedazur.multifidelity.interfaces.PointManagement;
import fr.univcotedazur.multifidelity.interfaces.SelectedAdvantageFinder;
import fr.univcotedazur.multifidelity.interfaces.SelectedAdvantageGetter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional(noRollbackFor = {NotAvailableAdvantageException.class, NotEnoughPointException.class})
public class GiftService implements AdvantageUsage, SelectedAdvantageGetter {

    @Autowired
    private IsawWhereYouParkedLastSummer isawWhereYouParkedLastSummer;

    @Autowired
    private AdvantageSelectedModifier advantageSelectedModifier;

    @Autowired
    private PointManagement pointManagement;

    @Autowired
    private SelectedAdvantageFinder selectedAdvantageFinder;

    @Autowired
    private NotificationSender notificationSender;


    public void setIsawWhereYouParkedLastSummer(IsawWhereYouParkedLastSummer isawWhereYouParkedLastSummer) {
        this.isawWhereYouParkedLastSummer = isawWhereYouParkedLastSummer;
    }


    @Override
    public SelectedAdvantage selectAdvantage(Advantage advantage, Consumer consumer) throws  NotEnoughPointException {
        Card card = consumer.getCard();
        if(card==null){
            throw new ResourceNotFoundException("no current carte found for consumer");
        }
        if(advantage.isVfpAdvantage() && !consumer.isVfp()){
            throw new NotAvailableAdvantageException("the consumer is not eligible to the advantage because advantage is vfp");
        }
        if(advantage.getValidity()!=null && advantage.getValidity().isParc() && StringUtils.isBlank(consumer.getLicencePlate())){
            throw new NotAvailableAdvantageException("the consumer must have enter his licence plate to select a parc advantage");
        }
        pointManagement.retrievePoints(advantage.getPoint(),card);
        return advantageSelectedModifier.addSelectAdvantage(advantage, card);
    }

    //TODO TEST  CUMCUMBER
    public void useParking(SelectedAdvantage selectedAdvantage){
        boolean status = isawWhereYouParkedLastSummer.startSession(selectedAdvantage.getCard().getConsumer());
        if(!status){
            throw new IWYPLSConnexionException("IWYPLS refused start session ");
        }
        advantageSelectedModifier.setState(selectedAdvantage,StateSelectedAdvantage.IN_PROCESSES);
    }

    @Override
    public SelectedAdvantage useAdvantage(SelectedAdvantage selectedAdvantage) throws NotAvailableAdvantageException,IWYPLSConnexionException {
        if(selectedAdvantage.getState()!= StateSelectedAdvantage.AVAILABLE){
            throw new NotAvailableAdvantageException("the gift can't be received it may have already been received ");
        }
        Validity validity = selectedAdvantage.getAdvantage().getValidity();
        if(validity!=null){
            if(!checkValidityDuration(selectedAdvantage)){
                advantageSelectedModifier.setState(selectedAdvantage,StateSelectedAdvantage.NOT_AVAILABLE);
                throw new NotAvailableAdvantageException("the gift has expire");
            }
            else if(!checkValidityNbUse(selectedAdvantage)){
                throw new NotAvailableAdvantageException("you have use more "+validity.getNumberOfUse()+" times you gift");
            }
            else {

                if(validity.isParc())
                    //TODO TEST CUCUMBER
                    useParking(selectedAdvantage);
                advantageSelectedModifier.incrementNbUse(selectedAdvantage,validity.getDurationPerUseBetweenUse());
            }
        }else{
            advantageSelectedModifier.setState(selectedAdvantage,StateSelectedAdvantage.NOT_AVAILABLE);
        }
        advantageSelectedModifier.setLastUse(LocalDateTime.now(),selectedAdvantage);
        return selectedAdvantage;
    }


    //TODO TEST  CUMCUMBER
    @Override
    public void terminateAdvantage(Consumer consumer, ParcDto parcDto) throws NotAvailableAdvantageException {
        Optional<SelectedAdvantage> selectedAdvantage = getAllSelectedAdvantages().stream().filter(selectedAdv -> Objects.equals(selectedAdv.getCard().getConsumer().getId(), consumer.getId())
                && selectedAdv.getState()==StateSelectedAdvantage.IN_PROCESSES && selectedAdv.getAdvantage().getValidity()!=null && selectedAdv.getAdvantage().getValidity().isParc()).findFirst();
        if(selectedAdvantage.isPresent()){
            if(parcDto.getRemainingTime()==0){
                advantageSelectedModifier.setState(selectedAdvantage.get(),StateSelectedAdvantage.AVAILABLE);
            }
            notificationSender.notifyConsumerEndParc(consumer,parcDto.getRemainingTime());
            return;

        }
        throw new ResourceNotFoundException("no current parking session found for this license plate");

    }


    @Override
    @Transactional(readOnly = true)
    public List<SelectedAdvantage> getAllSelectedAdvantageEligibleByConsumer(Consumer consumer) {
        List<SelectedAdvantage> selectedAdvantages = getAllSelectedAdvantages().stream().filter(selectedAdvantage -> selectedAdvantage.getCard().getConsumer().getId()==consumer.getId()).collect(Collectors.toList());
        selectedAdvantages =  selectedAdvantages.stream().filter(selectedAdvantage -> checkValidityDuration(selectedAdvantage) && checkValidityNbUse(selectedAdvantage)).collect(Collectors.toList());
        return selectedAdvantages.stream().filter(selectedAdvantage -> selectedAdvantage.getState()==StateSelectedAdvantage.AVAILABLE).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<SelectedAdvantage> getAllSelectedAdvantages() {
        return selectedAdvantageFinder.getAllSelectedAdvantages();
    }


    @Override
    @Transactional(readOnly = true)
    public SelectedAdvantage getSelectedAdvantageById(Long id) throws ResourceNotFoundException {
        return selectedAdvantageFinder.findSelectedAdvantageById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("No selected advantage found for id: %s", id.toString())));
    }



    public boolean checkValidityDuration(SelectedAdvantage selectedAdvantage){
        Validity validity = selectedAdvantage.getAdvantage().getValidity();
        if(validity!=null){
            Duration duration =validity.getDuration();
            LocalDateTime creation = selectedAdvantage.getCreation();
            return creation.plus(duration).isAfter(LocalDateTime.now());
        }
        return true;
    }


    public boolean checkValidityNbUse(SelectedAdvantage selectedAdvantage){
        Validity validity = selectedAdvantage.getAdvantage().getValidity();
        if(validity!=null){
            int nbPerDay = validity.getNumberOfUse();
            int nbUse = selectedAdvantage.getNumberOfUse();

            if (nbPerDay != 0) {
                LocalDateTime lastUse = selectedAdvantage.getLastUse();
                Duration durationPerUseBetweenUse = validity.getDurationPerUseBetweenUse();
                LocalDateTime now = LocalDateTime.now();
                if (lastUse == null) {
                    // Si l'avantage n'a jamais été utilisé
                    return true;
                }
                if (lastUse.isBefore(now.minus(durationPerUseBetweenUse))) {
                    // Si l'avantage n'a pas été utilisé dans la journée
                    return true;
                } else if (nbUse < nbPerDay) {
                    // Si le nombre d'utilisations de l'avantage n'a pas atteint la limite quotidienne
                    return true;
                } else {
                    // Si le nombre d'utilisations de l'avantage a atteint la limite quotidienne
                    return false;
                }
            }
        }
        // Si le nombre d'utilisations quotidiennes n'est pas limité
        return true;
    }
}
