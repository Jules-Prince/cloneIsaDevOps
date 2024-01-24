package fr.univcotedazur.multifidelity.controllers.dto_in;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Duration;

public class ValidityDtoIn {

    @Schema(name = "numberOfUse", description = "Number of use of the validity", type = "integer", example = "1")
    private int numberOfUse;
    @Schema(name = "durationPerUseBetweenUse", description = "Duration between two use of the validity", type = "string", example = "PT1H")
    private Duration durationPerUseBetweenUse;

    @Schema(name = "duration", description = "Duration of the validity", type = "string", example = "PT1H")
    private Duration duration;

    public ValidityDtoIn(int numberOfUse, Duration durationPerUseBetweenUse, Duration duration) {
        this.numberOfUse = numberOfUse;
        this.durationPerUseBetweenUse = durationPerUseBetweenUse;
        this.duration = duration;
    }

    public ValidityDtoIn() {
        // default constructor
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

    public int getNumberOfUse() {
        return numberOfUse;
    }

    public void setNumberOfUse(int numberOfUse) {
        this.numberOfUse = numberOfUse;
    }
}
