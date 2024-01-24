package fr.univcotedazur.multifidelity.exceptions;

public class NotEnoughBalanceException extends RuntimeException {

    public NotEnoughBalanceException(String msg) {
        super(msg);
    }


}