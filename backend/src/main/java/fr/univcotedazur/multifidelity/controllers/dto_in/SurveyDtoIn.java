package fr.univcotedazur.multifidelity.controllers.dto_in;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;

public class SurveyDtoIn {

    @Schema(name = "name", description = "name of the survey ", type = "String", example = "Survey 1")
    @NotBlank(message = "a survey must have a name")
    private String name;

    @Schema(name = "description", description = "description of the survey ", type = "String", example = "Survey 1")
    @NotBlank(message = "a survey must have a description")
    private String description;

    @ArraySchema(schema = @Schema(name = "questions", description = "questions of the survey", type = STRING_TYPE, example = "Etes vous satisfait de l'application ?"))
    @NotEmpty(message = "a survey must have a least one question")
    private List<String> questions;

    public SurveyDtoIn() {
        // default constructor
    }

    public SurveyDtoIn(String name, String description,  List<String> questions) {
        this.name = name;
        this.description = description;
        this.questions = questions;
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

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
