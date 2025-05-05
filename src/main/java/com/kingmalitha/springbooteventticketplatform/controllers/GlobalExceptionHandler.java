package com.kingmalitha.springbooteventticketplatform.controllers;

import com.kingmalitha.springbooteventticketplatform.domain.dtos.ErrorDto;
import com.kingmalitha.springbooteventticketplatform.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketNotFoundException(
            TicketNotFoundException ex
    ) {
        log.error("Caught TicketNotFoundException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Ticket not found");
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QrCodeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleQrCodeNotFoundException(
            QrCodeNotFoundException ex
    ) {
        log.error("Caught QrCodeNotFoundException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Qr Code not found");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(TicketSoldOutException.class)
    public ResponseEntity<ErrorDto> handleTicketSoldOutException(
            TicketSoldOutException ex
    ) {
        log.error("Caught TicketSoldOutException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("QrCode Generation Error");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<ErrorDto> handleQrCodeGenerationException(
            QrCodeGenerationException ex
    ) {
        log.error("Caught QrCodeGenerationException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("QrCode Generation Error");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEventNotFoundException(
            EventNotFoundException ex
    ) {
        log.error("Caught EventNotFoundException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Event not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketTypeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketTypeNotFoundException(
            TicketTypeNotFoundException ex
    ) {
        log.error("Caught TicketTypeNotFoundException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Ticket Type not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventUpdateException.class)
    public ResponseEntity<ErrorDto> handleEventUpdateException(
            EventUpdateException ex
    ) {
        log.error("Caught EventUpdateException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Event Update Error");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(
            UserNotFoundException ex
    ) {
        log.error("Caught UserNotFoundException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("User not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
           MethodArgumentNotValidException ex
    ) {
        log.error("Caught MethodArgumentNotValidException: {}", ex.getMessage(),
                ex);
        ErrorDto errorDto = new ErrorDto();
        String validationError = ex.getBindingResult().getFieldErrors()
                .stream().findFirst()
                .map(
                fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()
        ).orElse("Validation error occurred");

        errorDto.setError(validationError);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto>  handleConstraintViolationException(
            ConstraintViolationException ex
    ) {
        log.error("Caught ConstraintViolationException: {}", ex.getMessage(),
                ex);
        ErrorDto errorDto = new ErrorDto();

        String constraintViolationError = ex.getConstraintViolations()
                .stream().findFirst()
                .map(
                violation -> violation.getPropertyPath() + ": " + violation.getMessage()
        ).orElse("Constraint violation error");

        errorDto.setError(constraintViolationError);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Caught Exception: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("An unexpected error occurred. Please try again later.");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
