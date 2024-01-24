package fr.univcotedazur.multifidelity.controllers.dto_in;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;



public class SocietyDtoIn {


    @Schema(name = "name", description = "name of the society ", type = STRING_TYPE, example = "Pizza fiesta")
    @NotBlank(message = "name should not be blank")
    private String name;

    @Schema(name = "partnerId", description = "id of the owner of the society ", type = LONG_TYPE, example = "1L")
    @NotNull(message = "the owner id must be not null")
    private Long partnerId;

    @Schema(name = "openingHour", description = "Date and Time of the opening of the society", type = "time", example = "07:57:22")
    private LocalTime openingHour;

    @Schema(name = "closingHour", description = "Date and Time of the closing of the society", type = "time", example = "17:57:22")
    private LocalTime closingHour;

    @Schema(name = "address", description = "address of the society ", type = STRING_TYPE, example = "12 rue Matis")
    @NotBlank(message = "address should not be blank")
    private String address;

    public SocietyDtoIn() {
        // default constructor
    }

    public SocietyDtoIn(String name, Long partnerId, LocalTime openingHour, LocalTime closingHour, String address) {
        this.name = name;
        this.partnerId = partnerId;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long ownerId) {
        this.partnerId = ownerId;
    }

    public LocalTime getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(LocalTime openingHour) {
        this.openingHour = openingHour;
    }

    public LocalTime getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(LocalTime closingHour) {
        this.closingHour = closingHour;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
