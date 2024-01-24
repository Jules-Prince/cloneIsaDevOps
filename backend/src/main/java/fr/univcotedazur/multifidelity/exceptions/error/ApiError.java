package fr.univcotedazur.multifidelity.exceptions.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

import static fr.univcotedazur.multifidelity.constant.Constants.Common.DATE_FORMAT;
import static fr.univcotedazur.multifidelity.constant.Constants.Common.STRING_TYPE;
public class ApiError implements Serializable {

    @Schema(name = "timestamp", description = "timestamp of the error", type =DATE_FORMAT,example = "2023-02-23T09:26:18.305")
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;


    @Schema(name = "status", description = "status of the error", type =STRING_TYPE,example = "NOT_FOUND")
    @JsonProperty("status")
    private HttpStatus status;


    @Schema(name = "message", description = "message of the error", type =STRING_TYPE,example = "No action found for the given id : 100")
    @JsonProperty("message")
    private String message;


    public ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(String message) {
        this();
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
