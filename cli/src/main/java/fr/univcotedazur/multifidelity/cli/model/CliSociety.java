package fr.univcotedazur.multifidelity.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;


@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class CliSociety {

    private Long id ;

    private String name;

    private Long partnerId;

    private LocalTime openingHour = LocalTime.of(8, 0, 0, 0);

    private LocalTime closingHour = LocalTime.of(18, 0, 0, 0);

    private String address;

    public CliSociety(String name, Long partnerId, LocalTime openingHour, LocalTime closingHour, String address) {
        this.name = name;
        this.partnerId = partnerId;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Society {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", partnerId='" + partnerId.toString() + '\'' +
                ", openingHour='" + openingHour + '\'' +
                ", closingHour='" + closingHour + '\'' +
                ", adress='" + address + '\'' +
                '}';
    }
}
