package fr.univcotedazur.multifidelity.controllers.dto_in;

import fr.univcotedazur.multifidelity.controllers.dto_out.ValidityDtoOut;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.BOOLEAN_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.FLOAT_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.INTEGER_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.UUID_TYPE;

public class AdvantageDtoIn {

    @Schema (name = "name", description = "Name of the advantage ", type = STRING_TYPE, example = "Coca")
    @NotBlank(message = "message must be not blanck")
    private String name;

    @Schema (name = "point", description = "Point of the advantage ", type = INTEGER_TYPE, example = "10")
    @Min(value = 0,message = "the point must be positive")
    private int point;

    @Schema (name = "isVfpAdvantage", description = "Is the advantage a VFP advantage ", type = BOOLEAN_TYPE, example = "true")
    private boolean isVfpAdvantage;


    @Schema (name = "price", description = "Price of the advantage ", type = FLOAT_TYPE, example = "1.5")
    @Min(value = 0,message = "the price must be positive")
    private float price;

    @Schema (name = "initialPrice", description = "Initial price of the advantage ", type = FLOAT_TYPE, example = "1.5")
    @Min(value = 0,message = "the initial price must be positive")
    private float initialPrice;


    @Schema (name = "societyId", description = "id of the society of the advantage ", type = UUID_TYPE, example = "1124d9e8-6266-4bcf-8035-37a02ba75c69")
    private Long societyId;


    @Schema (name = "validity", description = "validity of the advantage ", type = "ValidityDtoOut", example = "")
    private ValidityDtoOut validity;

    public AdvantageDtoIn() {
        // default constructor
    }

    public AdvantageDtoIn(String name, int point, boolean isVfpAdvantage, float price, float initialPrice, Long societyId) {
        this.name = name;
        this.point = point;
        this.isVfpAdvantage = isVfpAdvantage;
        this.price = price;
        this.initialPrice = initialPrice;
        this.societyId = societyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isVfpAdvantage() {
        return isVfpAdvantage;
    }

    public void setVfpAdvantage(boolean vfpAdvantage) {
        isVfpAdvantage = vfpAdvantage;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(float initialPrice) {
        this.initialPrice = initialPrice;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }


    public ValidityDtoOut getValidity() {
        return validity;
    }

    public void setValidity(ValidityDtoOut validity) {
        this.validity = validity;
    }
}
