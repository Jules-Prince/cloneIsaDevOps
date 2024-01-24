package fr.univcotedazur.multifidelity.controllers.dto_out;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.DATE_FORMAT;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.INTEGER_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;



public class SelectedAdvantageDtoOut {

    @Schema(name = "id", description = "Id of the selectedAdvantage ", type = LONG_TYPE, example = "1L")
    private Long id;

    @Schema(name = "state", description = "state of the selectedAdvantage ", type = STRING_TYPE, example = "RETRY")
    private String state;

    @Schema(name = "cardId", description = "Id of the card of the selectedAdvantage ", type = LONG_TYPE, example = "1L")
    private Long cardId;

    @Schema(name = "advantageId", description = "Id of the avantage of the selectedAdvantage ", type = LONG_TYPE, example = "1L")
    private Long advantageId;

    @Schema(name = "lastUse", description = "date the last use of the selectedAdvantage ", type = DATE_FORMAT, example = "2022-09-30 07:57:22")
    private LocalDateTime lastUse;

    @Schema(name = "numberOfUse", description = "numberOfUse of the selectedAdvantage ", type = INTEGER_TYPE, example = "1")
    private int numberOfUse;

    public SelectedAdvantageDtoOut() {
        //  default constructor
    }

    public SelectedAdvantageDtoOut(Long id, String state, Long cardId, Long advantageId, LocalDateTime lastUse) {
        this.id = id;
        this.state = state;
        this.cardId = cardId;
        this.advantageId = advantageId;
        this.lastUse = lastUse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getAdvantageId() {
        return advantageId;
    }

    public void setAdvantageId(Long advantageId) {
        this.advantageId = advantageId;
    }

    public LocalDateTime getLastUse() {
        return lastUse;
    }

    public void setLastUse(LocalDateTime lastUse) {
        this.lastUse = lastUse;
    }

    public int getNumberOfUse() {
        return numberOfUse;
    }

    public void setNumberOfUse(int numberOfUse) {
        this.numberOfUse = numberOfUse;
    }
}
