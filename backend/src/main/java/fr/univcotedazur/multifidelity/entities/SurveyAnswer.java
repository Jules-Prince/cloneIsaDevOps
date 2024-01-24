package fr.univcotedazur.multifidelity.entities;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "survey_answer", schema = "public")
public class SurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Survey survey;
    private LocalDate date;


    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<SatisfactionType> answers = new ArrayList<>();

    public SurveyAnswer(Consumer consumer, List<SatisfactionType> answers) {
        this.consumer = consumer;
        this.answers = answers;
    }

    public SurveyAnswer(Consumer consumer, Survey survey, List<SatisfactionType> answers) {
        this.consumer = consumer;
        this.survey = survey;
        this.answers = answers;
    }

    public SurveyAnswer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyAnswer that = (SurveyAnswer) o;
        return Objects.equals(consumer, that.consumer) && Objects.equals(survey, that.survey) && Objects.equals(date, that.date) && Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consumer, survey, date, answers);
    }
}
