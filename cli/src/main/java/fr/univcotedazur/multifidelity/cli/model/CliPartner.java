package fr.univcotedazur.multifidelity.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CliPartner {

    private Long id;

    private String name;

    private String email;

    private String password;

    public CliPartner(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public CliPartner(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Partner {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
