package fr.univcotedazur.multifidelity.cli.model;

public enum CliSatisfactionType {
    VERY_SATISFIED(5), SATISFIED(4), NEUTRAL(3), UNSATISFIED(2), VERY_UNSATISFIED(1);

    private int point;

    CliSatisfactionType(int point){
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

}

