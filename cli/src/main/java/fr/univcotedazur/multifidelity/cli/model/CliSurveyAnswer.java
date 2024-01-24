package fr.univcotedazur.multifidelity.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class CliSurveyAnswer {
    private Long id;
    private Long consumerId;

    private Long surveyId;

    private LocalDate date;

    private List<CliSatisfactionType> answers;

    public CliSurveyAnswer(Long consumerId, Long surveyId, List<CliSatisfactionType> answers) {
        this.consumerId = consumerId;
        this.surveyId = surveyId;
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "SurveyAnswer {" +
                "id='" + id + '\'' +
                ", consumerId='" + consumerId + '\'' +
                ", surveyId='" + surveyId + '\'' +
                ", answers='" + answers + '\'' +
                '}';
    }
}
