package fr.univcotedazur.multifidelity.entities;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "purchase", schema = "public")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private float amount;

    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    private Card card;

    public Purchase(float amount, LocalDateTime date, Society society, Card card) {
        this.amount = amount;
        this.date = date;
        this.society = society;
        this.card = card;
    }

    public Purchase() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Float.compare(purchase.amount, amount) == 0 && Objects.equals(date, purchase.date) && Objects.equals(society, purchase.society) && Objects.equals(card, purchase.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, date, society, card);
    }
}
