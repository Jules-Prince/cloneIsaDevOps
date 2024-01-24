package fr.univcotedazur.multifidelity.entities;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Duration;
import java.util.Objects;


@Entity
@Table(name = "validity", schema = "public")
public class Validity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int numberOfUse; //si on peut l'utiliser une fois par jour -> 1
    private Duration durationPerUseBetweenUse; //si on peut l'utiliser une fois par jour -> 24h
    private Duration duration; //si on peut l'utiliser une fois par jour pendant une semaine -> 7j

    private boolean parc;


    public Validity() {
    }

    public Validity(int numberOfUse, Duration durationPerUseBetweenUse, Duration duration, Long id) {
        this.numberOfUse = numberOfUse;
        this.durationPerUseBetweenUse = durationPerUseBetweenUse;
        this.duration = duration;
        this.id = id;
        this.parc=false;
    }

    public Validity(int numberOfUse, Duration durationPerUseBetweenUse, Duration duration,boolean isParc ) {
        this.numberOfUse = numberOfUse;
        this.durationPerUseBetweenUse = durationPerUseBetweenUse;
        this.duration = duration;
        this.parc=isParc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Validity validity = (Validity) o;
        return duration==validity.duration && numberOfUse ==validity.numberOfUse && durationPerUseBetweenUse==validity.durationPerUseBetweenUse && parc==validity.parc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfUse,duration,durationPerUseBetweenUse,parc);
    }

    public int getNumberOfUse() {
        return numberOfUse;
    }

    public void setNumberOfUse(int numberOfUsePerDay) {
        this.numberOfUse = numberOfUsePerDay;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Duration getDurationPerUseBetweenUse() {
        return durationPerUseBetweenUse;
    }

    public void setDurationPerUseBetweenUse(Duration durationPerUseBetweenUse) {
        this.durationPerUseBetweenUse = durationPerUseBetweenUse;
    }


    public boolean isParc() {
        return parc;
    }

    public void setParc(boolean parc) {
        this.parc = parc;
    }
}
