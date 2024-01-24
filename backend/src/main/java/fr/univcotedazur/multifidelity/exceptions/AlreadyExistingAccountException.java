package fr.univcotedazur.multifidelity.exceptions;

public class AlreadyExistingAccountException extends RuntimeException {

    public AlreadyExistingAccountException (String msg) {
        super( msg);
    }


}
