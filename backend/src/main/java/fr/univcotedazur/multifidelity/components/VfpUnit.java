package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.interfaces.ConsumerFinder;
import fr.univcotedazur.multifidelity.interfaces.NotificationSender;
import fr.univcotedazur.multifidelity.interfaces.VfpChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
public class VfpUnit implements VfpChecker {

    @Autowired
    private ConsumerFinder consumerFinder;


    @Autowired
    private NotificationSender notificationSender;


    @Scheduled(cron = "0 */5 * * * *")
    public void VFPCheck() {
        consumerFinder.getAllConsumer().forEach(consumer -> {
            if(consumer.getCard()!=null){
                double FrequencyPurchaseByDayLastWeek= getFrequencyPurchaseByConsumer(consumer);
                boolean isVfp = FrequencyPurchaseByDayLastWeek>= (double) 1;
                consumer.setVfp(isVfp);
                if(FrequencyPurchaseByDayLastWeek <= 1.5){
                    notificationSender.notifyConsumerForVFP(consumer);
                }
            }
        });
    }

    @Override
    public void VFPCheckByConsumer(Consumer consumer) {
        double FrequencyPurchaseByDayLastWeek= getFrequencyPurchaseByConsumer(consumer);
        boolean isVfp = FrequencyPurchaseByDayLastWeek>= (double) 1;
        if(consumer.isVfp() && FrequencyPurchaseByDayLastWeek <= 1.5){
            notificationSender.notifyConsumerForVFP(consumer);
        }
        consumer.setVfp(isVfp);
    }


    public double getFrequencyPurchaseByConsumer(Consumer consumer) {
        List<Purchase> purchases = consumer.getCard().getPurchases();
        LocalDateTime now = LocalDateTime.now().plusHours(1);
        LocalDateTime weekAgo = now.minusWeeks(1);
        return (purchases.stream().filter(purchase ->
                purchase.getDate().isAfter(weekAgo) && purchase.getDate().isBefore(now)).count())/(double)7;
    }


}
