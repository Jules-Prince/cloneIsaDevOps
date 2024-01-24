package fr.univcotedazur.multifidelity.cli.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CliPurchase {

    private Long id;

    private float amount;

    private LocalDateTime date;

    private Long societyId;

    private Long cardId;

    public CliPurchase(float amount, LocalDateTime date, Long societyId, Long cardId) {
        this.amount = amount;
        this.date = date;
        this.societyId = societyId;
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "Purchase {" +
                "id='" + id + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", societyId='" + societyId.toString() + '\'' +
                ", cardId='" + cardId.toString() + '\'' +
                '}';
    }
}
