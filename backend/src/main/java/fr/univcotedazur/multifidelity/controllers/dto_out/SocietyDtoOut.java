package fr.univcotedazur.multifidelity.controllers.dto_out;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.DATE_FORMAT;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;


public class SocietyDtoOut {

    @Schema(name = "id", description = "Id of the society ", type = LONG_TYPE, example = "1L")
    private Long id ;

    @Schema(name = "name", description = "name of the society ", type = STRING_TYPE, example = "Pizza fiesta")
    private String name;

    @Schema(name = "partnerId", description = "id of the owner of the society ", type = LONG_TYPE, example = "1L")
    private Long partnerId;

    @Schema(name = "openingHour", description = "Date and Time of the opening of the society", type = DATE_FORMAT, example = "10:15:30")
    private LocalTime openingHour ;

    @Schema(name = "closingHour", description = "Date and Time of the closing of the society", type = DATE_FORMAT, example = "18:15:30")
    private LocalTime closingHour;

    @Schema(name = "address", description = "address of the society ", type = STRING_TYPE, example = "12 rue Matis")
    private String address;

    public SocietyDtoOut() {
        //  default constructor
    }

    public SocietyDtoOut(Long id, String name, Long ownerId, LocalTime openingHour, LocalTime closingHour, String address) {
        this.id = id;
        this.name = name;
        this.partnerId = ownerId;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
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
