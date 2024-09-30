package com.PCA.Others;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorControl implements ErrorController {

    @RequestMapping("/error")
    public String handleError(@RequestParam(value = "message", required = false) String errorMessage, Model model) {
        if (errorMessage == null) {
            errorMessage = "Ha ocurrido un error inesperado. Por favor, inténtelo de nuevo más tarde.";
        }
        model.addAttribute("errorMessage", errorMessage);

        return "error";
    }

}
