package com.patternatlas.api.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessageDTO {

    protected Integer status;
    protected String message;
    protected ZonedDateTime time;

    public ErrorMessageDTO(String message) {
        this.message = message;
        this.time = ZonedDateTime.now();
    }

    public ErrorMessageDTO(String message, int status) {
        this(message);
        this.status = status;
    }

    public ErrorMessageDTO(String message, HttpStatus status) {
        this(message);
        this.status = status.value();
    }
}
