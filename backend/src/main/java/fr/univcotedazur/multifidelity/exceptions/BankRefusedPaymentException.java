package fr.univcotedazur.multifidelity.exceptions;

public class BankRefusedPaymentException extends RuntimeException {

    public BankRefusedPaymentException(String msg) {
        super(msg);
    }

}