package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AccountNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.ConsumerAccountManager;
import fr.univcotedazur.multifidelity.interfaces.ConsumerFinder;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConsumerManager implements ConsumerFinder, ConsumerAccountManager  {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Consumer> findConsumerById(Long id) {
        return consumerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Consumer> findConsumerByEmail(String email) {
        return consumerRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Consumer> findConsumerByPlate(String plate) {
        return consumerRepository.findByLicencePlate(plate);
    }

    @Override
    @Transactional(readOnly = true)
    public Consumer researchConsumer(String email, String password) throws AccountNotFoundException {
        Optional<Consumer> consumer = consumerRepository.findAll().stream()
                .filter(c -> c.getEmail().equals(email) && c.getPassword().equals(password)).findAny();
        if(consumer.isPresent())
            return consumer.get();
        throw new AccountNotFoundException("No existing account with the given info");
    }

    @Override
    public Consumer addConsumer(Consumer newConsumer) {
        return consumerRepository.save(newConsumer);

    }

    @Override
    public Consumer updateConsumer(Consumer oldConsumer, Consumer newConsumer) {
        oldConsumer.setEmail(newConsumer.getEmail());
        oldConsumer.setPassword(newConsumer.getPassword());
        oldConsumer.setCreditCardNumber(newConsumer.getCreditCardNumber());
        oldConsumer.setLicencePlate(newConsumer.getLicencePlate());
        return oldConsumer;
    }

    @Override
    public Consumer addFavSociety(Society society, Consumer consumer) {
        consumer.getFavSocieties().add(society);
        return consumer;
    }

    @Override
    public Consumer deleteFavSociety(Society society, Consumer consumer) {
        consumer.getFavSocieties().remove(society);
        return consumer;
    }

    @Override
    public Consumer setVfp(Consumer consumer, boolean isVfp) {
        consumer.setVfp(isVfp);
        return consumer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Consumer> getAllConsumer() {
        return new ArrayList<>(consumerRepository.findAll());
    }


    @Override
    @Transactional(readOnly = true)
    public void deleteAllConsumer() {
        consumerRepository.deleteAll();
    }

}
