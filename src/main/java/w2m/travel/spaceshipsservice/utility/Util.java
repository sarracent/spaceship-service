package w2m.travel.spaceshipsservice.utility;

import java.util.*;

/**
 * The type Util.
 */
public class Util {
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
