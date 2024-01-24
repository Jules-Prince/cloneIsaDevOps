package fr.univcotedazur.multifidelity.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "society", schema = "public")
public class Society {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Partner partner;

    private LocalTime openingHour = LocalTime.of(8, 0, 0, 0);

    private LocalTime closingHour = LocalTime.of(18, 0, 0, 0);

    private String address;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "society",cascade= CascadeType.REMOVE)
    private List<Advantage> advantages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "society",cascade= CascadeType.REMOVE)
    private List<Purchase> purchases = new ArrayList<>();

    @ManyToMany(mappedBy = "favSocieties",cascade= CascadeType.REMOVE)
    private List<Consumer> fans = new ArrayList<>();

    public Society(String name, Partner partner, LocalTime openingHour, LocalTime closingHour, String address) {
        this.name = name;
        this.partner = partner;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.address = address;
    }

    public Society() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner owner) {
        this.partner = owner;
    }

    public LocalTime getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(LocalTime openingHour) {
        this.openingHour = openingHour;
    }

    public LocalTime getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(LocalTime closingHour) {
        this.closingHour = closingHour;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Society society = (Society) o;
        return Objects.equals(name, society.name) && Objects.equals(partner, society.partner) && Objects.equals(openingHour, society.openingHour) && Objects.equals(closingHour, society.closingHour) && Objects.equals(address, society.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, partner, openingHour, closingHour, address);
    }

    public List<Advantage> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(List<Advantage> advantages) {
        this.advantages = advantages;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public List<Consumer> getFans() {
        return fans;
    }

    public void setFans(List<Consumer> fans) {
        this.fans = fans;
    }
}
