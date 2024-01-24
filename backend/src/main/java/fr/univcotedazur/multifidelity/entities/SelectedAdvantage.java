package fr.univcotedazur.multifidelity.entities;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "selected_advantage", schema = "public")
public class SelectedAdvantage {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StateSelectedAdvantage state;

    @ManyToOne(fetch = FetchType.LAZY)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    private Advantage advantage;


    private LocalDateTime creation;

    private LocalDateTime lastUse ;

    private int numberOfUse;

    public SelectedAdvantage() {
    }

    public SelectedAdvantage(StateSelectedAdvantage state, Card card, Advantage advantage, LocalDateTime lastUse) {
        this.state = state;
        this.card = card;
        this.advantage = advantage;
        this.lastUse = lastUse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StateSelectedAdvantage getState() {
        return state;
    }

    public void setState(StateSelectedAdvantage state) {
        this.state = state;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Advantage getAdvantage() {
        return advantage;
    }

    public void setAdvantage(Advantage advantage) {
        this.advantage = advantage;
    }

    public LocalDateTime getLastUse() {
        return lastUse;
    }

    public void setLastUse(LocalDateTime lastUse) {
        this.lastUse = lastUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectedAdvantage that = (SelectedAdvantage) o;
        return numberOfUse==that.numberOfUse &&state == that.state && Objects.equals(card, that.card) && Objects.equals(advantage, that.advantage) && Objects.equals(lastUse, that.lastUse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfUse,state, card, advantage, lastUse);
    }

    public int getNumberOfUse() {
        return numberOfUse;
    }

    public void setNumberOfUse(int numberOfUse) {
        this.numberOfUse = numberOfUse;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }
}
