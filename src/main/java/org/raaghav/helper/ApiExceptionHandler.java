package org.raaghav.helper;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    //other exception handlers

    @ExceptionHandler(TradeRejectedException.class)
    protected ResponseEntity<Object> handleException(
            TradeRejectedException ex) {
        ApiException apiException = new ApiException("TRADE_REJECTED", ex.getMessage());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}