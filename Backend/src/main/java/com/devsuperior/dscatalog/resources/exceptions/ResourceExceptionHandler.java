package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {
    //classe manipuladora de exceções da camada de resources/controllers

    @ExceptionHandler(ResourceNotFoundException.class) //intercepta o lancamento de execoes desse tipo
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException error, HttpServletRequest req){

        StandardError standardError = new StandardError();

        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.NOT_FOUND.value());
        standardError.setError("Recurso nao encontrado");
        standardError.setMessage(error.getMessage());
        standardError.setPath(req.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
    }

    @ExceptionHandler(DatabaseException.class) //intercepta o lancamento de execoes desse tipo
    public ResponseEntity<StandardError> database(DatabaseException error, HttpServletRequest req){

        StandardError standardError = new StandardError();

        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.BAD_REQUEST.value());
        standardError.setError("database exception");
        standardError.setMessage(error.getMessage());
        standardError.setPath(req.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }

}
