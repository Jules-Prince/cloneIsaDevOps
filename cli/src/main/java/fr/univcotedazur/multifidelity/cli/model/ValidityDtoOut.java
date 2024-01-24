package fr.univcotedazur.multifidelity.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidityDtoOut {


    private int numberOfUse;

    private Duration durationPerUseBetweenUse;

    private Duration duration;

    private boolean isParc;


    @Override
    public String toString() {
        return "Validity {" +
                ", numberOfUse='" + numberOfUse + '\'' +
                ", durationPerUseBetweenUse='" + durationPerUseBetweenUse + '\'' +
                ", duration='" + duration + '\'' +
                ", isParc='" + isParc + '\'' +
                '}';
    }
}
