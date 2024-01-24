package fr.univcotedazur.multifidelity.controllers.dto_out;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Duration;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.BOOLEAN_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.INTEGER_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;

public class ValidityDtoOut {

    @Schema(name = "numberOfUse", description = "numberOfUse of the advantage ", type = INTEGER_TYPE, example = "1")
    private int numberOfUse;

    @Schema(name = "durationPerUseBetweenUse", description = "durationPerUseBetweenUse of the advantage ", type = STRING_TYPE, example = "PT1H40M")
    private Duration durationPerUseBetweenUse;

    @Schema(name = "duration", description = "duration total of the advantage ", type = STRING_TYPE, example = "PT1H40M")
    private Duration duration;

    @Schema(name = "isParc", description = "true if is a parc advantage ", type = BOOLEAN_TYPE, example = "true")
    private boolean isParc;

    public ValidityDtoOut() {
        // default constructor
    }
    public ValidityDtoOut(int numberOfUse, Duration durationPerUseBetweenUse, Duration duration, boolean isParc) {
        this.numberOfUse = numberOfUse;
        this.durationPerUseBetweenUse = durationPerUseBetweenUse;
        this.duration = duration;
        this.isParc = isParc;
    }

    public int getNumberOfUse() {
        return numberOfUse;
    }

    public void setNumberOfUse(int numberOfUse) {
        this.numberOfUse = numberOfUse;
    }

    public Duration getDurationPerUseBetweenUse() {
        return durationPerUseBetweenUse;
    }

    public void setDurationPerUseBetweenUse(Duration durationPerUseBetweenUse) {
        this.durationPerUseBetweenUse = durationPerUseBetweenUse;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public boolean isParc() {
        return isParc;
    }

    public void setParc(boolean parc) {
        isParc = parc;
    }
}
