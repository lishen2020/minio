package com.minio.ls.util;

/**
 * String Utils
 *
 * @author lishen
 * @version 1.0
 * @date 2020/07/23
 */
public class StringUtils {

    /**
     * empty string
     */
    public static final String EMPTY = "";
    /**
     * string of eq (=)
     */
    public static final String EQUAL = "=";
    /**
     * string of &
     */
    public static final String AND = "&";
    /**
     * string of /
     */
    public static final String SLASH = "/";
    /**
     * string of ?
     */
    public static final String QUESTION_MARK = "?";

    /**
     * string of .
     */
    public static final String DOT = ".";

    /**
     * Checks that the specified string reference is not {@code null} and
     * throws a customized {@link NullPointerException} if it is.
     *
     * @param str     the string reference to check for nullity
     * @param message detail message to be used in the event that a {@code NullPointerException} is thrown
     * @return {@link String} {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     * @author lishen
     */
    public static String requireNonNullOrEmpty(String str, String message) {
        if (trimIsEmpty(str)) {
            throw new NullPointerException(message);
        }
        return str;
    }

    /**
     * string is null or empty
     *
     * @param str
     * @return {@link boolean} true:is null or empty, false:not null or empty
     * @author lishen
     */
    public static boolean isEmpty(String str) {
        return str == null || EMPTY.equals(str);
    }

    /**
     * trim string is null or empty
     *
     * @param str
     * @return {@link boolean} true:is null or empty, false:not null or empty
     * @author lishen
     */
    public static boolean trimIsEmpty(String str) {
        return str == null || EMPTY.equals(str.trim());
    }

}
