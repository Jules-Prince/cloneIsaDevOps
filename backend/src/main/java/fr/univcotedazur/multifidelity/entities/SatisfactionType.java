package fr.univcotedazur.multifidelity.entities;


public enum SatisfactionType {
    VERY_SATISFIED(5), SATISFIED(4), NEUTRAL(3), UNSATISFIED(2), VERY_UNSATISFIED(1);

    private int point;

    SatisfactionType(int point){
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
