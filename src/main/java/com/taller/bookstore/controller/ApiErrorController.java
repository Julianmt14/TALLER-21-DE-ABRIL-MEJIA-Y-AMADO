package com.taller.bookstore.controller;

import com.taller.bookstore.dto.response.ApiErrorResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ApiErrorResponse> handleError(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        HttpStatus status = resolveStatus(statusCode);
        String path = requestUri != null ? requestUri.toString() : request.getRequestURI();
        String message = resolveMessage(status, errorMessage);

        return ResponseEntity.status(status)
                .body(ApiErrorResponse.of(status.value(), message, List.of(message), path));
    }

    private HttpStatus resolveStatus(Object statusCode) {
        if (statusCode instanceof Integer code) {
            return HttpStatus.resolve(code) != null ? HttpStatus.valueOf(code) : HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String resolveMessage(HttpStatus status, Object errorMessage) {
        if (errorMessage != null && !errorMessage.toString().isBlank()) {
            return errorMessage.toString();
        }
        return switch (status) {
            case BAD_REQUEST -> "Solicitud invalida";
            case NOT_FOUND -> "Recurso no encontrado";
            case METHOD_NOT_ALLOWED -> "Metodo HTTP no permitido";
            default -> "Error interno del servidor";
        };
    }
}
