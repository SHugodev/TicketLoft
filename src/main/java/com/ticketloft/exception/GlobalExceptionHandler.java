package com.ticketloft.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public String handleConcurrencyException(ObjectOptimisticLockingFailureException ex, Model model) {
        model.addAttribute("error",
                "Lo sentimos, las entradas que intentabas reservar se han agotado o han cambiado de precio mientras realizabas la operación. Por favor, inténtalo de nuevo.");
        model.addAttribute("status", 409);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("error", "Ha ocurrido un error inesperado: " + ex.getMessage());
        model.addAttribute("status", 500);
        return "error";
    }
}
