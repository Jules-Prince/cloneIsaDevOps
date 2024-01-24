package fr.univcotedazur.multifidelity.controllers.dto_out;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.LOCAL_DATE_TIME_PATTERN;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;


public class SurveyDtoOut {

    @Schema(name = "id", description = "Id of the survey ", type = LONG_TYPE, example = "1L")
    private Long id;

    @Schema(name = "name", description = "name of the survey ", type = STRING_TYPE, example = "survey1")
    private String name;

    @Schema(name = "description", description = "description of the survey ", type = STRING_TYPE, example = "survey1")
    private String description;

    @Schema(name = "date", description = "date of the survey ", type = LOCAL_DATE_TIME_PATTERN, example = "2021-06-02T21:33:45.249")
    private LocalDate date;

    @ArraySchema(schema = @Schema(name = "questions", description = "questions of the survey", type = STRING_TYPE, example = "Etes vous satisfait de l'application ?"))
    private List<String> questions;

    public SurveyDtoOut() {
    }

    public SurveyDtoOut(Long id, String name, String description, LocalDate date, List<String> questions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
