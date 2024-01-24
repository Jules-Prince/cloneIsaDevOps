package fr.univcotedazur.multifidelity.controllers.dto_out;

import io.swagger.v3.oas.annotations.media.Schema;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.FLOAT_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.INTEGER_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;



public class CardDtoOut {
    @Schema(name = "id", description = "id of the card ", type = LONG_TYPE, example = "1L")
    private Long id;

    @Schema(name = "point", description = "points on the card ", type = INTEGER_TYPE, example = "17")
    private int point;

    @Schema(name = "balance", description = "balance on the card ", type = FLOAT_TYPE, example = "17,37")
    private float balance;

    @Schema(name = "ownerId", description = "id of the owner", type = LONG_TYPE, example = "1")
    private Long ownerId;

    public CardDtoOut() {
        // default constructor
    }

    public CardDtoOut(Long id, int point, float balance) {
        this.id = id;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
