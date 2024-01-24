package fr.univcotedazur.multifidelity.controllers.dto_in;


import fr.univcotedazur.multifidelity.entities.SatisfactionType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;


public class SurveyAnswerDtoIn {

    @Schema(name = "consumerId", description = "Id of the consumer who answer", type = LONG_TYPE, example = "1L")
    private Long consumerId;

    @Schema(name = "surveyId", description = "Id of the survey", type = LONG_TYPE, example = "1L")
    private Long surveyId;

    @ArraySchema(schema = @Schema(name = "answers", description = "answers of a survey", type = STRING_TYPE, example = "SATISFIED"))
    @NotEmpty(message = "a survey answer must have at least a answers")
    private List<SatisfactionType> answers;

    public SurveyAnswerDtoIn() {
        // default constructor
    }
    public SurveyAnswerDtoIn(Long consumerId, Long surveyId, List<SatisfactionType> answers) {
        this.consumerId = consumerId;
        this.surveyId = surveyId;
        this.answers = answers;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public List<SatisfactionType> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SatisfactionType> answers) {
        this.answers = answers;
    }
}
