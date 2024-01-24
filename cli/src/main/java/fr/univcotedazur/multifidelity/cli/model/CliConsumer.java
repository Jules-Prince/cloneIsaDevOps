package fr.univcotedazur.multifidelity.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CliConsumer {

    private Long id ;

    private String email;

    private String password;

    private String creditCardNumber;

    private String licencePlate;

    private boolean isVfp;

    private List<Long> favSocietyIds = new ArrayList<>();

    private Long cardId;


    public CliConsumer(String email, String password, String creditCardNumber, String licencePlate) {
        this.email = email;
        this.password = password;
        this.creditCardNumber = creditCardNumber;
        this.licencePlate = licencePlate;
    }

    public CliConsumer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Consumer {" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", licencePlate='" + licencePlate + '\'' +
                ", isVfp='" + isVfp + '\'' +
                ", favSocietyIds='" + favSocietyIds + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
