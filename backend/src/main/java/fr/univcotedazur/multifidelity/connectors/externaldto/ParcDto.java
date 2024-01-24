package fr.univcotedazur.multifidelity.connectors.externaldto;

public class ParcDto {

    private String plate;


    private int remainingTime;

    public ParcDto(String licencePlate, int remainingTimeMinute) {
        this.plate = licencePlate;
        this.remainingTime = remainingTimeMinute;
    }

    public ParcDto() {
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }


    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
}
