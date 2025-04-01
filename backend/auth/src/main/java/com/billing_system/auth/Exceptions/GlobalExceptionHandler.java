package com.billing_system.auth.Exceptions;

import com.billing_system.auth.DTO.Response.ExceptionResponseDTO;
import com.billing_system.auth.Utils.DateFormat;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final DateFormat dateFormat;

    @Autowired
    public GlobalExceptionHandler(DateFormat dateFormat){
        this.dateFormat = dateFormat;
    }

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<ExceptionResponseDTO> UserAlreadyRegisterExceptionHandler(UserAlreadyRegisterException exception, HttpServletRequest request){
      return new ResponseEntity<>(getExceptionResponseDTO("P-400", "UserAlreadyRegister", exception.getMessage(), request), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<ExceptionResponseDTO> UserNotExistException(UserNotExistException exception, HttpServletRequest request){
      return new ResponseEntity<>(getExceptionResponseDTO("P-404", "UserNotExist", exception.getMessage(), request), HttpStatus.NOT_FOUND);
    };

    private ExceptionResponseDTO getExceptionResponseDTO(String code, String exception, String message, HttpServletRequest request){
        ExceptionResponseDTO exceptionDTO = new ExceptionResponseDTO();
        exceptionDTO.setCode(code);
        exceptionDTO.setException(exception);
        exceptionDTO.setMessage(message);
        exceptionDTO.setUri(request.getRequestURI());
        exceptionDTO.setTimestamp(dateFormat.getDate());
        return exceptionDTO;
    }
}