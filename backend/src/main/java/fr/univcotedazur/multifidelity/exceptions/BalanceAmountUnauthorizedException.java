package fr.univcotedazur.multifidelity.exceptions;

public class BalanceAmountUnauthorizedException extends RuntimeException{

    public BalanceAmountUnauthorizedException (String msg) {
        super( msg);
    }
}
