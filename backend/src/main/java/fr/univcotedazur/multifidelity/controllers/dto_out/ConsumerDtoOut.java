package fr.univcotedazur.multifidelity.controllers.dto_out;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.BOOLEAN_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.INTEGER_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.LONG_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.UUID_TYPE;


public class ConsumerDtoOut {

    @Schema(name = "id", description = "Id of the consumer ", type = UUID_TYPE, example = "1124d9e8-6266-4bcf-8035-37a02ba75c69")
    private Long id ;

    @Schema(name = "email", description = "email of the consumer ", type = STRING_TYPE, example = "clairemarini06390@gmail.com")
    private String email;

    @Schema(name = "password", description = "password of the consumer ", type = STRING_TYPE, example = "claire06")
    private String password;

    @Schema(name = "creditCardNumber", description = "credit card number of the consumer ", type = STRING_TYPE, example = "1737475968")
    private String creditCardNumber;

    @Schema(name = "licencePlate", description = "licence plate of the consumer ", type = STRING_TYPE, example = "12-AZE-62")
    private String licencePlate;


    @ArraySchema(schema = @Schema(name = "favSocietyIds", description = "list of the ids of the fav societies of the consumer", type = INTEGER_TYPE, example = "3"))
    private List<Long> favSocietyIds;

    @ArraySchema(schema = @Schema(name = "favSocietyIds", description = "list of the ids of the fav societies of the consumer", type = INTEGER_TYPE, example = "3"))

    @Schema(name = "isVfp", description = "true if the consumer is vfp ", type = BOOLEAN_TYPE, example = "true")
    private boolean isVfp;

    @Schema(name = "cardId", description = "id of the card of the consumer ", type = LONG_TYPE, example = "1L")
    private Long cardId;

    public ConsumerDtoOut() {
        // default constructor
    }

    public ConsumerDtoOut(Long id, String email, String password, String creditCardNumber, String licencePlate, Long cardId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.creditCardNumber = creditCardNumber;
        this.licencePlate = licencePlate;
        this.cardId =cardId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Long> getFavSocietyIds() {
        return favSocietyIds;
    }

    public void setFavSocietyIds(List<Long> favSocietyIds) {
        this.favSocietyIds = favSocietyIds;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public boolean isVfp() {
        return isVfp;
    }

    public void setVfp(boolean vfp) {
        isVfp = vfp;
    }
}