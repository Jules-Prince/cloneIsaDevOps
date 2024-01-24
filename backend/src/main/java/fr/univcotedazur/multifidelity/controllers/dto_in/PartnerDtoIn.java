package fr.univcotedazur.multifidelity.controllers.dto_in;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;
public class PartnerDtoIn {

    @Schema(name = "name", description = "name of the consumer ", type = STRING_TYPE, example = "claire")
    @NotBlank(message = "name should not be blank")
    private String name;

    @Schema(name = "email", description = "email of the consumer ", type = STRING_TYPE, example = "clairemarini06390@gmail.com")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "email must have a pattern")
    private String email;

    @Schema(name = "password", description = "password of the consumer ", type = STRING_TYPE, example = "claire06")
    @NotBlank(message = "password should not be blank")
    private String password;

    public PartnerDtoIn() {
        // default constructor
    }

    public PartnerDtoIn(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
