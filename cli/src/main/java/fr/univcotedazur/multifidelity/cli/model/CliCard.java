package fr.univcotedazur.multifidelity.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CliCard {

    private Long id ;

    private int point;

    private float balance;

    private int limitPayment;

    private Long ownerId;

    public CliCard(int point, float balance, int limitPayment, Long owner){
        this.point = point;
        this.balance = balance;
        this.limitPayment = limitPayment;
        this.ownerId = owner;
    }

    @Override
    public String toString() {
        return "Card {" +
                "id='" + id + '\'' +
                ", point='" + point + '\'' +
                ", balance='" + balance + '\'' +
                ", limitPayment='" + limitPayment + '\'' +
                ", ownerId='" + ownerId.toString() + '\'' +
                '}';
    }
}
