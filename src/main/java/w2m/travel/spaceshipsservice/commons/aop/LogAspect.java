package w2m.travel.spaceshipsservice.commons.aop;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import w2m.travel.spaceshipsservice.annotations.auditable.AuditableApi;
import w2m.travel.spaceshipsservice.annotations.log.LogException;
import w2m.travel.spaceshipsservice.annotations.log.LogIgnore;
import w2m.travel.spaceshipsservice.constants.Constants;
import w2m.travel.spaceshipsservice.constants.Errors;
import w2m.travel.spaceshipsservice.exception.CustomException;
import w2m.travel.spaceshipsservice.model.response.ServiceResponse;
import w2m.travel.spaceshipsservice.utility.LogUtil;
import w2m.travel.spaceshipsservice.utility.Logs;
import w2m.travel.spaceshipsservice.utility.Util;

import static w2m.travel.spaceshipsservice.constants.Constants.*;
import static w2m.travel.spaceshipsservice.utility.Logs.Basic.*;
import static w2m.travel.spaceshipsservice.utility.Logs.Extras.START_TIME;
import static w2m.travel.spaceshipsservice.utility.Logs.Extras.ERROR_EXECUTE;
import static w2m.travel.spaceshipsservice.utility.Logs.Header.TRANSACTION_ID;

@Aspect
@Component
public class LogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    LogAspect() {
        LogUtil.initializeStructure(EnumSet.allOf(Logs.Header.class), EnumSet.allOf(Logs.Basic.class));
    }

    public static String getExecutionTime() {
        final long start = ThreadContext.get(START_TIME.name()) == null ? 0 : Long.parseLong(ThreadContext.get(START_TIME.name()));
        return String.valueOf(System.currentTimeMillis() - start);
    }

    public static void logFinishOperationInError(Object response) {
        if (response instanceof ServiceResponse) {
            LogUtil.logFinishOperation(LogAspect.LOG, ((ServiceResponse) response).getResultCode(), ((ServiceResponse) response).getResultMessage(), getExecutionTime());
            LogUtil.logCleanAll();
        }
    }

    @Around("@annotation(w2m.travel.spaceshipsservice.annotations.auditable.AuditableApi)")
    public Object logExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        ThreadContext.put(START_TIME.name(), String.valueOf((System.currentTimeMillis())));
        ThreadContext.put(ERROR_EXECUTE.name(), N);

        AuditableApi auditableApi = ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(AuditableApi.class);
        String nameHttpHeadersMap = auditableApi.parameterIgnore().nameToAudit();
        Map<String, Object> parametersMap = getMethodParameters(jp);
        parametersMap.remove(nameHttpHeadersMap);

        updateLogParameter(getOperation(jp), jp.getSignature().getName(), parametersMap);

        Object response = jp.proceed();

        ThreadContext.put(RESPONSE.name(), getResponse(response));

        LogUtil.logFinishOperation(LOG, Constants.OK_VALUE, Constants.OK_MSG, getExecutionTime());
        LogUtil.logCleanAll();

        return response;
    }

    @Around("@annotation(w2m.travel.spaceshipsservice.annotations.log.LogService)")
    public Object logOperationTime(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = jp.proceed();
        long executionTime = System.currentTimeMillis() - start;
        LogUtil.logIntermediateOperation(LOG, getOperation(jp), Constants.OK_MSG, Constants.SUCCESS_MSG, String.valueOf(executionTime), getRequest(jp), Util.toJSONString(proceed));
        return proceed;
    }

    @AfterReturning(value = "@annotation(org.springframework.web.bind.annotation.ExceptionHandler)", returning = "response")
    public void logFinishOperationInError(JoinPoint jp, Object response) {
        ServiceResponse serviceResponse = null;
        if (response instanceof ResponseEntity) {
            serviceResponse = (ServiceResponse)((ResponseEntity<?>)response).getBody();
        }

        if (response instanceof ServiceResponse) {
            serviceResponse = (ServiceResponse)response;
        }

        if (serviceResponse != null) {
            LogUtil.logFinishOperation(LOG, serviceResponse.getResultCode(), serviceResponse.getResultMessage(), getExecutionTime());
        }

        LogUtil.cleanThreadContext();
    }

    @AfterThrowing(pointcut = "logForAll()", throwing = "ex")
    public void afterAnyException(JoinPoint jp, Exception ex) {
        String errorExecute = Optional.ofNullable(ThreadContext.get(ERROR_EXECUTE.name())).orElse(N);
        Class<?> exceptionClass = ex.getClass();
        if (!(exceptionClass.isAnnotationPresent(LogException.class))) {
            if (errorExecute.equalsIgnoreCase(Y)) return;
            else ThreadContext.put(ERROR_EXECUTE.name(), Y);
        }
        String opName = getOperation(jp);
        String code;
        String message;
        if (ex instanceof CustomException) {
            var customException = ((CustomException) ex);
            code = String.valueOf(customException.getCode());
            message = customException.getMessage();
        } else {
            code = Errors.ERROR_GENERAL.getCode();
            message = String.format(Errors.ERROR_GENERAL.getMessage(), ex.getMessage(), ex);
        }
        String request = getRequest(jp);
        LogUtil.logErrorOperation(LOG, opName, code, message, getExecutionTime(), request, getResponseWithError(message, ex));
    }

    @Pointcut("within(w2m.travel.spaceshipsservice..*)")
    private void logForAll() {
        // Escucha en todos los metodos que pasen por el negocio
    }

    private Map<String, Object> getMethodParameters(ProceedingJoinPoint joinPoint) {
        try {
            Parameter[] parameters = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
            if (parameters == null || parameters.length == 0) return new HashMap<>();
            return IntStream.range(0, parameters.length)
                    .boxed()
                    .filter(index -> (parameters[index].getAnnotation(LogIgnore.class) == null))
                    .map(index -> new AbstractMap.SimpleEntry<>(parameters[index].getName(),
                            joinPoint.getArgs()[index] != null ? joinPoint.getArgs()[index] : EMPTY)
                    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (Exception ex) {
            return new HashMap<>();
        }
    }

    private String getOperation(Object operation) {
        try {
            if (operation instanceof ProceedingJoinPoint) {
                ProceedingJoinPoint joinPoint = (ProceedingJoinPoint) operation;
                return String.format(Constants.OPNAME,
                        joinPoint.getSignature().getDeclaringType().getSimpleName(),
                        joinPoint.getSignature().getName());
            }
            return operation.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    private String getRequest(Object request) {
        try {
            if (request instanceof ProceedingJoinPoint) {
                ProceedingJoinPoint joinPoint = (ProceedingJoinPoint) request;
                AuditableApi auditableApi = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(AuditableApi.class);
                Map<String, Object> parametersMap = getMethodParameters(joinPoint);
                if (auditableApi != null) parametersMap.remove(auditableApi.parameterIgnore().nameToAudit());
                return Util.toJSONString(parametersMap);
            }
            return Util.toJSONString(request);
        } catch (Exception ex) {
            return null;
        }
    }

    private String getResponse(Object response) {
        try {
            if (response instanceof ResponseEntity)
                return Util.toJSONString(((ResponseEntity<?>) response).getBody());
            if (response instanceof Throwable)
                return Arrays.stream(((Throwable) response).getStackTrace()).findFirst().map(StackTraceElement::toString).orElse("");
            return Util.toJSONString(response);
        } catch (Exception ex) {
            return null;
        }
    }

    private String getResponseWithError(String message, Throwable throwable) {
        try {
            return Util.getMsgWithLineCodeError(message, throwable);
        } catch (Exception ex) {
            return null;
        }
    }

    private void updateLogParameter(String operationName, String methodName, Map<String, Object> parametersMap) {
        final String transactionId = Util.generateUniqueId(methodName);

        ThreadContext.put(TRANSACTION_ID.name(), transactionId);
        ThreadContext.put(OPERATION.name(), operationName);
        ThreadContext.put(REQUEST.name(), getRequest(parametersMap));
    }

}
