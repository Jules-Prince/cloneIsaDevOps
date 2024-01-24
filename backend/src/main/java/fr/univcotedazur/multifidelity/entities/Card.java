package fr.univcotedazur.multifidelity.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "card", schema = "public")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    private int point;

    private float balance;

    @OneToOne(mappedBy = "card")
    private Consumer consumer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "card", cascade = CascadeType.REMOVE)
    private List<Purchase> purchases = new ArrayList<>();


    public Card() {
    }

    public Card( int point, float balance) {
        this.point = point;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return point == card.point && Float.compare(card.balance, balance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, balance);
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }
}
