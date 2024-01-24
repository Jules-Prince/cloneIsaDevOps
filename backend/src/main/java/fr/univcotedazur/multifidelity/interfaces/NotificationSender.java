package fr.univcotedazur.multifidelity.interfaces;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.ScheduleType;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.Survey;

import java.time.LocalTime;

public interface NotificationSender {

    void notifyConsumerNewSchedule(LocalTime schedule , Society society , ScheduleType scheduleType);

    void notifyConsumerForVFP(Consumer consumer);

    void sendSurveyToConsumer(Survey survey, Consumer consumer);

    void notifyConsumerEndParc(Consumer consumer, int remindingTime);

}
