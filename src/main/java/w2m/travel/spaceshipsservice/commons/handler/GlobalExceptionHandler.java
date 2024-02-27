package w2m.travel.spaceshipsservice.commons.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import w2m.travel.spaceshipsservice.commons.aop.LogAspect;
import w2m.travel.spaceshipsservice.commons.resolver.CustomExceptionResolverDelegate;
import w2m.travel.spaceshipsservice.exception.impl.*;
import w2m.travel.spaceshipsservice.model.response.ServiceResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ServiceResponse> handleMethodArgumentNotValidException(Exception exception, WebRequest request) {
        final var serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(exception);
        LogAspect.logFinishOperationInError(serviceResponse);
        return ResponseEntity.badRequest().body(serviceResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ServiceResponse> handleResourceNotFoundException(Exception exception, WebRequest reques) {
        final var serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(exception);
        LogAspect.logFinishOperationInError(serviceResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ServiceResponse> handleAllExceptions(Exception exception, WebRequest request) {
        final var serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(exception);
        LogAspect.logFinishOperationInError(serviceResponse);
        return ResponseEntity.internalServerError().body(serviceResponse);
    }


}