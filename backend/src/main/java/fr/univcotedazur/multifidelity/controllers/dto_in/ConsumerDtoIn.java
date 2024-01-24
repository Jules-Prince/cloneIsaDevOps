package fr.univcotedazur.multifidelity.controllers.dto_in;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;

public class ConsumerDtoIn {

    @Schema(name = "email", description = "email of the consumer ", type = STRING_TYPE, example = "clairemarini06390@gmail.com")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "email must have a pattern")
    @NotBlank(message = "email should not be blank")
    private String email;

    @Schema(name = "password", description = "password of the consumer ", type = STRING_TYPE, example = "claire06")
    @NotBlank(message = "password should not be blank")
    private String password;

    @Schema(name = "creditCardNumber", description = "credit card number of the consumer ", type = STRING_TYPE, example = "1737475968")
    @Pattern(regexp = "\\d{16}+", message = "credit card should be exactly 16 digits")
    private String creditCardNumber;

    @Schema(name = "licencePlate", description = "licence plate of the consumer ", type = STRING_TYPE, example = "12AZE62")
    private String licencePlate;

    public ConsumerDtoIn() {
        // default constructor
    }
    public ConsumerDtoIn(String email, String password, String creditCardNumber, String licencePlate) {
        this.email = email;
        this.password = password;
        this.creditCardNumber = creditCardNumber;
        this.licencePlate = licencePlate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }
}
