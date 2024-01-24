package fr.univcotedazur.multifidelity.exceptions;

import fr.univcotedazur.multifidelity.exceptions.error.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler  {


    @ExceptionHandler(value
            = { ResourceNotFoundException.class,AlreadyExistingAccountException.class, AlreadyExistSimilarRessourceException.class})
    public ResponseEntity<ApiError> handleNotFoundException(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiError apiError = new ApiError(e.getMessage());
        apiError.setStatus(httpStatus);
        return createResponseEntity(apiError, httpStatus);
    }

    @ExceptionHandler(value
            = {NotAvailableAdvantageException.class })
    public ResponseEntity<ApiError> handleNotAvailableAdvantageException(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        ApiError apiError = new ApiError(e.getMessage());
        apiError.setStatus(httpStatus);
        return createResponseEntity(apiError, httpStatus);
    }

    @ExceptionHandler(value
            = { AccountNotFoundException.class })
    public ResponseEntity<ApiError> handleNotAccountNotFoundException(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiError apiError = new ApiError(e.getMessage());
        apiError.setStatus(httpStatus);
        return createResponseEntity(apiError, httpStatus);
    }


    @ExceptionHandler(value
            = { BalanceAmountUnauthorizedException.class,NotEnoughBalanceException.class,NotEnoughPointException.class ,BankRefusedPaymentException.class ,InvalidAnswerSurveyException.class})
    public ResponseEntity<ApiError> handleUnauthorized(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiError apiError = new ApiError(e.getMessage());
        apiError.setStatus(httpStatus);
        return createResponseEntity(apiError, httpStatus);

    }

    @ExceptionHandler(value
            = { BankConnexionException.class,IWYPLSConnexionException.class })
    public ResponseEntity<ApiError> handleBadGateway(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;
        ApiError apiError = new ApiError(e.getMessage());
        apiError.setStatus(httpStatus);
        return createResponseEntity(apiError, httpStatus);
    }




    private ResponseEntity<ApiError> createResponseEntity(ApiError apiError, HttpStatus httpStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiError, headers, httpStatus);
    }

}
