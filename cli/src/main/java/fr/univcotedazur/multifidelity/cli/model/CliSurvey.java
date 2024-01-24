package fr.univcotedazur.multifidelity.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class CliSurvey {

    private Long id ;

    private String name;

    private String description;

    private LocalDate date;

    private List<String> questions;

    public CliSurvey(String name, String description,  List<String> questions) {
        this.name = name;
        this.description = description;
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Survey {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
