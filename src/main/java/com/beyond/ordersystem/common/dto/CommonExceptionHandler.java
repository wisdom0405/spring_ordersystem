package com.beyond.ordersystem.common.dto;

import com.beyond.ordersystem.common.dto.CommonErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

//ControllerAdivice - ExceptionHandler 는 한세트다.
@ControllerAdvice
public class CommonExceptionHandler {

//    Controller단에서 발생하는 모든 EntityNotFoundException catch
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonErrorDto> entityNotFoundHandler(EntityNotFoundException e){
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(commonErrorDto,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonErrorDto> illegalHandler(IllegalArgumentException e){
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonErrorDto> validHandler(MethodArgumentNotValidException e){
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(),"arguments not valid");
        return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
    }
}
