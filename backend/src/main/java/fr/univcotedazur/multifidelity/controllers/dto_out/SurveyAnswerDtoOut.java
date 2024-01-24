package fr.univcotedazur.multifidelity.controllers.dto_out;

import fr.univcotedazur.multifidelity.entities.SatisfactionType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.LOCAL_DATE_TIME_PATTERN;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;


public class SurveyAnswerDtoOut {

    @Schema(name = "id", description = "Id of the survey answer", type = LONG_TYPE, example = "1L")
    private Long id;

    @Schema(name = "consumerId", description = "Id of the consumer who answer", type = LONG_TYPE, example = "1L")
    private Long consumerId;

    @Schema(name = "surveyId", description = "Id of the survey", type = LONG_TYPE, example = "1L")
    private Long surveyId;

    @Schema(name = "date", description = "date of the survey answer", type = LOCAL_DATE_TIME_PATTERN, example = "2021-06-02T21:33:45.249")
    private LocalDate date;

    @ArraySchema(schema = @Schema(name = "answers", description = "answers of a survey", type = STRING_TYPE, example = "SATISFAIT"))
    private List<SatisfactionType> answers;

    public SurveyAnswerDtoOut() {
    }

    public SurveyAnswerDtoOut(Long id, Long consumerId, Long surveyId, LocalDate date, List<SatisfactionType> answers) {
        this.id = id;
        this.consumerId = consumerId;
        this.surveyId = surveyId;
        this.date = date;
        this.answers = answers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<SatisfactionType> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SatisfactionType> answers) {
        this.answers = answers;
    }
}
