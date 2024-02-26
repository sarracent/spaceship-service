package w2m.travel.spaceshipsservice.exception.impl;

import w2m.travel.spaceshipsservice.exception.CustomException;
import w2m.travel.spaceshipsservice.exception.ExceptionType;

import java.util.Collections;
import java.util.List;

public class ResourceNotFoundException extends RuntimeException implements CustomException {
    private static final long serialVersionUID = -1132348466005485433L;
    private final String code;
    private final String message;
    private final transient List<Object> extraInfo;

    public ResourceNotFoundException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.extraInfo = Collections.emptyList();
    }

    public ResourceNotFoundException(String code, String message, List<Object> extraInfo) {
        super(message);
        this.code = code;
        this.message = message;
        this.extraInfo = extraInfo;
    }

    @Override
    public ExceptionType getExceptionType() {
        return ExceptionType.EXTERNAL_EXCEPTION;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<Object> getExtraInfo() {
        return extraInfo;
    }
}
