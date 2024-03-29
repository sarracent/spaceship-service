package w2m.travel.spaceshipsservice.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.*;

import static w2m.travel.spaceshipsservice.constants.Constants.PARSE_EXCEPTION;
import static w2m.travel.spaceshipsservice.constants.Errors.ERROR_GENERAL;

/**
 * The type Util.
 */
public class Util {
    private static final ObjectMapper mapperJson = getMapperJson();
    private Util() {
    }
    /**
     * Generate mapper without WRITE_DATES_AS_TIMESTAMPS.
     *
     * @return the static ObjectMapper
     */
    public static ObjectMapper getMapperJson() {
        ObjectMapper mapperJson = new ObjectMapper();
        mapperJson.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapperJson.registerModule(new JavaTimeModule());
        mapperJson.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapperJson;
    }

    /**
     * Generate unique id string.
     *
     * @param pKeyConcactToId the p key concact to id
     * @return the string
     */
    public static String generateUniqueId(String pKeyConcactToId) {
        if (isNullOrEmpty(pKeyConcactToId))
            return UUID.randomUUID().toString().replace("-", "");
        return UUID.randomUUID().toString().replace("-", "") + "-" + pKeyConcactToId;
    }
    /**
     * Convert message with throwable line code error to String.
     *
     * @param message   the message error
     * @param throwable the throwable to get line code error
     * @return the string
     */
    public static String getMsgWithLineCodeError(String message, Throwable throwable) {
        try {
            String lineCodeError = Arrays.stream(throwable.getStackTrace()).findFirst().map(StackTraceElement::toString).orElse(null);
            if (message == null) return lineCodeError;
            return String.format(PARSE_EXCEPTION, message, lineCodeError);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * Converts an Object to JSONString.
     *
     * @param input the input
     * @return the string
     */
    public static String toJSONString(Object input) {
        if (input == null) return null;
        try {
            return mapperJson.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            return String.format(ERROR_GENERAL.getMessage(), e.getMessage(), e);
        }
    }
    /**
     * Is null or empty.
     *
     * @param pText the p text
     * @return the boolean
     */
    public static boolean isNullOrEmpty(String pText) {
        return pText == null || pText.isEmpty();
    }

    /**
     * Is null or empty.
     *
     * @param list the List
     * @return the boolean
     */
    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
