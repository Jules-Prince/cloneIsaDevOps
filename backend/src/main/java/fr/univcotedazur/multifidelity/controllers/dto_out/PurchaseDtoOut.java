package fr.univcotedazur.multifidelity.controllers.dto_out;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.FLOAT_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LOCAL_DATE_TIME_PATTERN;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;


public class PurchaseDtoOut {
    @Schema(name = "id", description = "id of the purchase ", type = LONG_TYPE, example = "1L")
    private Long id;

    @Schema(name = "amount", description = "amount of the purchase ", type = FLOAT_TYPE, example = "17,37")
    private float amount;

    @Schema(name = "date", description = "date of the purchase ", type = LOCAL_DATE_TIME_PATTERN, example = "2021-06-02T21:33:45.249")
    private LocalDateTime date;

    @Schema(name = "societyId", description = "id of the society of the purchase", type = LONG_TYPE, example = "1L")
    private Long societyId;

    @Schema(name = "cardId", description = "id of the card used for the purchase", type = LONG_TYPE, example = "1L")
    private Long cardId;

    public PurchaseDtoOut(Long id, float amount, LocalDateTime date, Long societyId, Long cardId) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.societyId = societyId;
        this.cardId = cardId;
    }

    public PurchaseDtoOut() {
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

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
