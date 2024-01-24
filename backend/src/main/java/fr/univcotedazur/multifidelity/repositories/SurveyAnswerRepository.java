package fr.univcotedazur.multifidelity.repositories;

import fr.univcotedazur.multifidelity.entities.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {
}
