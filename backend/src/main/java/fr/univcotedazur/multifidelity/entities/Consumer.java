package fr.univcotedazur.multifidelity.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "consumer", schema = "public")
public class Consumer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    private String email;

    private String password;

    private String creditCardNumber;

    private String licencePlate;

    private boolean isVfp;

    @ManyToMany(fetch = FetchType.LAZY,cascade= CascadeType.REMOVE)
    @JoinTable(name = "fav_society", schema = "public", joinColumns = @JoinColumn(name = "consumer_id"), inverseJoinColumns = @JoinColumn(name = "society_id"))
    private List<Society> favSocieties = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade= CascadeType.REMOVE)
    private Card card;


    public Consumer(String email, String password, String creditCardNumber, String licencePlate,Card card) {
        this.email = email;
        this.password = password;
        this.creditCardNumber = creditCardNumber;
        this.licencePlate = licencePlate;
        this.favSocieties = new ArrayList<>();
        this.isVfp =false;
        this.card =card;
    }

    public Consumer() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public boolean isVfp() {
        return isVfp;
    }

    public void setVfp(boolean vfp) {
        isVfp = vfp;
    }

    public List<Society> getFavSocieties() {
        return favSocieties;
    }

    public void setFavSocieties(List<Society> favSocieties) {
        this.favSocieties = favSocieties;
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
        Consumer consumer = (Consumer) o;
        return isVfp == consumer.isVfp && Objects.equals(email, consumer.email) && Objects.equals(password, consumer.password) && Objects.equals(creditCardNumber, consumer.creditCardNumber) && Objects.equals(licencePlate, consumer.licencePlate) && Objects.equals(favSocieties, consumer.favSocieties)&& Objects.equals(card, consumer.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, creditCardNumber, licencePlate, isVfp, favSocieties,card);
    }
    
}
