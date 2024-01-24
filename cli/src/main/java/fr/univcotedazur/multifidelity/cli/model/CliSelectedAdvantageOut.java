package fr.univcotedazur.multifidelity.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CliSelectedAdvantageOut {

    private Long id;

    private CliStateUseAdvantage state;

    private Long cardId;

    private Long advantageId;

    private LocalDateTime lastUse ;

    private int numberOfUse;

    public CliSelectedAdvantageOut(CliStateUseAdvantage state, Long cardId, Long advantageId) {
        this.state = state;
        this.cardId = cardId;
        this.advantageId = advantageId;
    }

    @Override
    public String toString() {
        return "CliSelectedAdvantageOut{" +
                "id=" + id +
                ", state=" + state +
                ", cardId=" + cardId +
                ", advantageId=" + advantageId +
                ", lastUse=" + lastUse +
                ", numberOfUse=" + numberOfUse +
                '}';
    }
}
