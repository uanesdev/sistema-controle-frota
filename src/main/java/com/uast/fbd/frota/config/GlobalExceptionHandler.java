package com.uast.fbd.frota.config;

import com.uast.fbd.frota.exception.RecursoNaoEncontradoException;
import com.uast.fbd.frota.exception.ValidacaoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<String> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        // Você pode retornar um objeto JSON para um corpo de erro mais rico
        return new ResponseEntity<>("Erro 404: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<String> handleValidacaoException(ValidacaoException ex) {
        return new ResponseEntity<>("Erro 400: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>("Erro 400: Parâmetro inválido. " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

