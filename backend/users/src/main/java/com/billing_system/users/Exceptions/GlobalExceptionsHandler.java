package com.billing_system.users.Exceptions;

import com.billing_system.users.DTO.Response.ExceptionResponseDTO;
import com.billing_system.users.utils.DateFormat;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionsHandler {
    private final DateFormat dateFormat;
    @Autowired
    public GlobalExceptionsHandler(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDTO> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        return new ResponseEntity<>(getExceptionResponseDTO("P-400", e, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponseDTO> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        return new ResponseEntity<>(getExceptionResponseDTO("P-400", e, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>(getExceptionResponseDTO("P-404",  e, request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        return new ResponseEntity<>(getExceptionResponseDTO("P-400", e, request), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleException(Exception e, HttpServletRequest request) {
        return new ResponseEntity<>(getExceptionResponseDTO("P-500", e, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ExceptionResponseDTO getExceptionResponseDTO(String code, Exception e, HttpServletRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setCode(code);
        exceptionResponseDTO.setException(e.getClass().getSimpleName());
        exceptionResponseDTO.setMessage(e.getMessage());
        exceptionResponseDTO.setUri(request.getRequestURI());
        exceptionResponseDTO.setTimestamp(dateFormat.getDate());
        return exceptionResponseDTO;
    }
}
