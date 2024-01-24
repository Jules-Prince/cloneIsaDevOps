package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.ScheduleType;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.NotificationSender;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.interfaces.SocietyModifier;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Transactional
public class SocietyManager implements SocietyGetter, SocietyModifier {

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private NotificationSender notificationSender;

    public Society getSocietyById(Long id) throws ResourceNotFoundException {
        return findSocietyById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("No society found for id: %s", id.toString())));
    }


    private Optional<Society> findSocietyById(Long id) {
        return societyRepository.findById(id);
    }


    @Override
    public Society updateSocietyInformation(Society oldSociety,Society newSociety)  {
        if (newSociety.getOpeningHour().compareTo(oldSociety.getOpeningHour()) != 0)
            notificationSender.notifyConsumerNewSchedule(newSociety.getOpeningHour(), newSociety, ScheduleType.OPENING);
        if (newSociety.getClosingHour().compareTo(oldSociety.getClosingHour()) != 0)
            notificationSender.notifyConsumerNewSchedule(newSociety.getClosingHour(), newSociety, ScheduleType.CLOSING);
        oldSociety.setAddress(newSociety.getAddress());
        oldSociety.setOpeningHour(newSociety.getOpeningHour());
        oldSociety.setClosingHour(newSociety.getClosingHour());
        return oldSociety;
    }

    @Override
    public Society addSociety(Society newSociety) throws AlreadyExistSimilarRessourceException {
        if (findSocietyByNameByPartner(newSociety.getName(),newSociety.getPartner()).isPresent()) {
            throw new AlreadyExistSimilarRessourceException(String.format("Society already exists with this name : %s",newSociety.getName()));
        }else {
            return societyRepository.save(newSociety);
        }
    }

    private Optional<Society> findSocietyByNameByPartner(String name, Partner partner) {
        return societyRepository.findAll().stream()
                .filter(society -> society.getPartner()==partner && name.equals(society.getName())).findFirst();
    }

    @Override
    @Cacheable("societies")
    @Transactional(readOnly = true)
    public List<Society> getAllSocieties() {
        return new ArrayList<>(societyRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Society getSocietyByName(String name) throws ResourceNotFoundException {
        return societyRepository.findAll().stream().filter(society -> Objects.equals(society.getName(), name)).findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format("No society found for name: %s", name)));
    }
}
