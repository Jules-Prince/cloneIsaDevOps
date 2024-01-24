package fr.univcotedazur.multifidelity.controllers.dto_in;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.FLOAT_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LOCAL_DATE_TIME_PATTERN;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.UUID_TYPE;



public class PurchaseDtoIn {

    @Schema(name = "amount", description = "amount of the purchase ", type = FLOAT_TYPE, example = "17,37")
    @Min(value = 0,message = "the amount must is positif")
    private float amount;

    @Schema(name = "date", description = "date of the purchase ", type = LOCAL_DATE_TIME_PATTERN, example = "2021-06-02T21:33:45.249")
    private LocalDateTime date;

    @Schema(name = "societyId", description = "id of the society of the purchase", type = UUID_TYPE, example = "1124d9e8-6266-4bcf-8035-37a02ba75c69")
    private Long societyId;

    @Schema(name = "cardId", description = "id of the card used for the purchase", type = LONG_TYPE, example = "1L")
    private Long cardId;

    public PurchaseDtoIn() {
        // default constructor
    }

    public PurchaseDtoIn(float amount, LocalDateTime date, Long societyId, Long cardId) {
        this.amount = amount;
        this.date = date;
        this.societyId = societyId;
        this.cardId = cardId;
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
