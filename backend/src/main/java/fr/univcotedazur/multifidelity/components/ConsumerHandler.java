package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.AccountNotFoundException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistingAccountException;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.interfaces.ConsumerAccountManager;
import fr.univcotedazur.multifidelity.interfaces.ConsumerFinder;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import fr.univcotedazur.multifidelity.interfaces.ConsumerModifier;
import fr.univcotedazur.multifidelity.interfaces.RegistryConsumerProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@Transactional
public class ConsumerHandler implements RegistryConsumerProcessor, ConsumerModifier, ConsumerGetter {

    @Autowired
    private ConsumerFinder consumerFinder;

    @Autowired
    private ConsumerAccountManager consumerAccountManager;


    @Override
    public Consumer logIn(String email, String password) throws AccountNotFoundException {
        return consumerAccountManager.researchConsumer(email, password);
    }

    @Override
    public Consumer signIn(String email, String password) throws AlreadyExistingAccountException {
        if(consumerFinder.findConsumerByEmail(email).isPresent())
            throw new AlreadyExistingAccountException(String.format("an account with this email already exists : %s",email));
        else{
            Consumer newConsumer = new Consumer(email, password, null, null,null);
            return consumerAccountManager.addConsumer(newConsumer);
        }
    }

    @Override
    public Consumer updateConsumer(Consumer oldConsumer,Consumer newConsumer) {
        if(!Objects.equals(newConsumer.getLicencePlate(), oldConsumer.getLicencePlate())){
            if(consumerFinder.findConsumerByPlate((newConsumer.getLicencePlate())).isPresent()){
                throw new AlreadyExistingAccountException("an cusoumer have already this licence plate");
            }
        }
       return consumerAccountManager.updateConsumer(oldConsumer, newConsumer);
    }

    @Override
    public Consumer addFavSociety(Society society, Consumer consumer) {
        return consumerAccountManager.addFavSociety(society,consumer);
    }

    @Override
    public Consumer deleteFavSociety(Society society, Consumer consumer) {
        return consumerAccountManager.deleteFavSociety(society,consumer);
    }


    @Override
    public Consumer getConsumerById(Long id) throws ResourceNotFoundException {
        return consumerFinder.findConsumerById(id).orElseThrow(()-> new ResourceNotFoundException(String.format("No consumer found for id: %s" , id.toString())));
    }

    @Override
    @Cacheable("consumers")
    public List<Consumer> getAllConsumer() {
        return consumerFinder.getAllConsumer();
    }


    public List<Society> getFavSocietiesByConsumer(Consumer consumer) {
        return consumer.getFavSocieties();
    }

    @Override
    public Consumer getConsumerByPlate(String plate) {
        return consumerFinder.findConsumerByPlate(plate).orElseThrow(()-> new ResourceNotFoundException(String.format("No consumer found for plate: %s" , plate)));
    }


    @Override
    public void deleteAllConsumer() {
        consumerAccountManager.deleteAllConsumer();
    }

    @Override
    public Consumer setVfp(Consumer consumer, boolean isVfp) {
        return consumerAccountManager.setVfp(consumer,isVfp);
    }

}
