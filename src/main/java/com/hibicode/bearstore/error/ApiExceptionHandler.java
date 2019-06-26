package com.hibicode.bearstore.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.hibicode.bearstore.service.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.hibicode.bearstore.error.ErrorResponse.ApiError;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice // Do Spring, com ele vc consegue interceptar as exceções lançadas pela aplicação em um lugar centralizado que vai ser essa classe.
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ApiExceptionHandler {

    private static final String NO_MESSAGE_AVAILABLE = "No message available";
//    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class); //usar o org.slf4j.Logger

    private final MessageSource apiErrorMessageSource; // o nome tem que ser o mesmo que foi criado no Bean de Configuração ApiErrorConfig para injetar pelo construtor dessa forma mais simples

    @ExceptionHandler(MethodArgumentNotValidException.class) // para capturar uma exceção argumento inválido
    public ResponseEntity<ErrorResponse> handleNotValidException(MethodArgumentNotValidException exception, Locale locale) {

        // exception.getBindingResult() pega os resultados do que aconteceram dessa requisição que acabou de chegar
        // transformamos em um Stream
        Stream<ObjectError> errors = exception.getBindingResult().getAllErrors().stream();

        // criamos uma lista de ApiError
        List<ApiError> apiErrors = errors
                .map(ObjectError::getDefaultMessage)
                .map(code -> toApiError(code, locale))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, apiErrors);
        return ResponseEntity.badRequest().body(errorResponse);

    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException exception, Locale locale) {

        final String errorCode = "generic-1";
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final ErrorResponse errorResponse = ErrorResponse.of(status, toApiError(errorCode, locale, exception.getValue()));

        return ResponseEntity.badRequest().body(errorResponse);

    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception, Locale locale) {

        final String errorCode = exception.getCode();
        final HttpStatus status = exception.getStatus();
        final ErrorResponse errorResponse = ErrorResponse.of(status, toApiError(errorCode, locale));

        return ResponseEntity.status(status).body(errorResponse);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception exception, Locale locale) {

        log.error("Error not expected");
        final String errorCode = "error-1";
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorResponse errorResponse = ErrorResponse.of(status, toApiError(errorCode, locale ));
        return ResponseEntity.status(status).body(errorResponse);


    }


    ApiError toApiError(String code, Locale locale, Object... args) {

        String message;
        try {
            message = apiErrorMessageSource.getMessage(code, args, locale);
        } catch(NoSuchMessageException e) {
            //LOG.error("Could not find any message for {} code under {} locale", code, locale);
            log.error("Could not find any message for {} code under {} locale", code, locale);
            message = NO_MESSAGE_AVAILABLE;
        }
        return new ApiError(code, message);
    }

}
