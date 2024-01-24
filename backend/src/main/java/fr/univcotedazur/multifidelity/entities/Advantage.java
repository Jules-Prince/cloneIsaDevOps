package fr.univcotedazur.multifidelity.entities;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "advantage", schema = "public")
public class Advantage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private float price;

    private int point;

    private boolean isVfpAdvantage;

    private float initialPrice;

    @OneToOne(fetch = FetchType.LAZY)
    private Validity validity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Society society;


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "advantage",cascade = CascadeType.REMOVE)
    private List<SelectedAdvantage> selectedAdvantages;

    public Advantage(String name, int point, boolean isVfpAdvantage, float initialPrice, Society society, float price) {
        this.name = name;
        this.point = point;
        this.isVfpAdvantage = isVfpAdvantage;
        this.initialPrice = initialPrice;
        this.society = society;
        this.price =price;
    }

    public Advantage() {
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isVfpAdvantage() {
        return isVfpAdvantage;
    }

    public void setVfpAdvantage(boolean vfpAdvantage) {
        isVfpAdvantage = vfpAdvantage;
    }

    public float getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(float initialPrice) {
        this.initialPrice = initialPrice;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advantage advantage = (Advantage) o;
        return Float.compare(advantage.price, price) == 0 && point == advantage.point && isVfpAdvantage == advantage.isVfpAdvantage && Float.compare(advantage.initialPrice, initialPrice) == 0 && Objects.equals(name, advantage.name) && Objects.equals(society, advantage.society);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, point, isVfpAdvantage, initialPrice, society);
    }

    public List<SelectedAdvantage> getSelectedAdvantages() {
        return selectedAdvantages;
    }

    public void setSelectedAdvantages(List<SelectedAdvantage> selectedAdvantages) {
        this.selectedAdvantages = selectedAdvantages;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }
}
