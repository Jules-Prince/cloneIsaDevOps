package fr.univcotedazur.multifidelity.exceptions.error;

public class ErrorDto {

    private String error;
    private String details;

    public ErrorDto() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
