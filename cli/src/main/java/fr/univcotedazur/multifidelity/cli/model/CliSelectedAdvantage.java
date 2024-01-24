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
public class CliSelectedAdvantage {

    private Long id;

    private CliStateUseAdvantage state;

    private CliCard card;

    private CliAdvantage advantage;

    private LocalDateTime lastUse ;

    public CliSelectedAdvantage(CliStateUseAdvantage state, CliCard card, CliAdvantage advantage) {
        this.state = state;
        this.card = card;
        this.advantage = advantage;
    }

    @Override
    public String toString() {
        return "CliSelectedAdvantage{" +
                "id=" + id +
                ", state=" + state +
                ", card=" + card +
                ", advantage=" + advantage +
                ", lastUse=" + lastUse +
                '}';
    }
}
