package w2m.travel.spaceshipsservice.commons.resolver;


import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import w2m.travel.spaceshipsservice.constants.Errors;
import w2m.travel.spaceshipsservice.exception.CustomException;
import w2m.travel.spaceshipsservice.model.response.ServiceResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CustomExceptionResolverDelegate {

    private CustomExceptionResolverDelegate() {
    }

    public static ServiceResponse buildServiceResponse(Exception ex) {
        if (ex instanceof CustomException) {
            CustomException customException = (CustomException) ex;
            return ServiceResponse.builder()
                    .resultCode(customException.getCode())
                    .resultMessage(customException.getMessage())
                    .build();

        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;

            List<String> errors = methodArgumentNotValidException.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ServiceResponse.builder()
                    .resultCode(Errors.ERROR_BAD_REQUEST.getCode())
                    .resultMessage(String.format(Errors.ERROR_BAD_REQUEST.getMessage(), errors))
                    .build();

        } else {
            return ServiceResponse.builder()
                    .resultCode(Errors.ERROR_GENERAL.getCode())
                    .resultMessage(String.format(Errors.ERROR_GENERAL.getMessage(), ex.getMessage(), ex))
                    .build();
        }
    }

}