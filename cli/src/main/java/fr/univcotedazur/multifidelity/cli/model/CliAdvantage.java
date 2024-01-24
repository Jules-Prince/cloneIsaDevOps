package fr.univcotedazur.multifidelity.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CliAdvantage {

    private Long id;

    private String name;

    private int point;

    private boolean isVfpAdvantage;

    private float initialPrice;

    private Long societyId;

    private float price;

    private ValidityDtoOut validity;

    public CliAdvantage(String name, int point, boolean isVfpAdvantage, float initialPrice, Long societyId, float price) {
        this.name = name;
        this.point = point;
        this.isVfpAdvantage = isVfpAdvantage;
        this.initialPrice = initialPrice;
        this.societyId = societyId;
        this.price =price;
    }

    @Override
    public String toString() {
        return "Advantage {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", point='" + point + '\'' +
                ", isVFPAdvantage='" + isVfpAdvantage + '\'' +
                ", initialPrice='" + initialPrice + '\'' +
                ", society='" + societyId.toString() + '\'' +
                ", price='" + price + '\'' +
                ", validity='" + validity!=null?validity.toString():"" + '\'' +
                '}';
    }
}
