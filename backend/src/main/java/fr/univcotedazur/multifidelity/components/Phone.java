package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.ScheduleType;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.interfaces.NotificationSender;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.logging.Logger;

@Component
public class Phone implements NotificationSender {

    Logger log = Logger.getLogger(NotificationSender.class.getName());

    @Override
    public void notifyConsumerNewSchedule(LocalTime schedule, Society society, ScheduleType scheduleType) {
        for(Consumer consumer : society.getFans()){
            log.info(String.format("The consumer %s has been notified of the store's schedule change %s",consumer.getEmail(),society.getName()));

        }
    }

    @Override
    public void notifyConsumerForVFP(Consumer consumer) {
        log.info(String.format("consumer with email : %s notified because he will soon lose his vfp status",consumer.getEmail()));
    }

    @Override
    public void sendSurveyToConsumer(Survey survey, Consumer consumer) {
        log.info(String.format("consumer with email : %s notified to answer the survey called %s.",consumer.getEmail(),survey.getName()));
    }

    @Override
    public void notifyConsumerEndParc(Consumer consumer, int remindingTime) {
        log.info(String.format("consumer with plate : %s notified because  there is still %s minutes of parking time ",consumer.getLicencePlate(),remindingTime));
    }

}
