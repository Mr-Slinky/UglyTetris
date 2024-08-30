package tetris.utility;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A class that houses various validation logic checks related to arguments.
 * This class contains static methods to validate common argument conditions and
 * throw an {@link IllegalArgumentException} if the conditions are not met.
 *
 * <p>
 * Example usage:
 * <pre>
 * String name = "example";
 * IllegalArgs.throwNull("name", name);
 * </pre>
 * </p>
 *
 * @since 1.0
 * @version 1.1
 *
 * @see IllegalArgumentException
 *
 * @author Kheagen Haskins
 */
public class IllegalArgs {

    /**
     * Throws an {@link IllegalArgumentException} if the specified object is
     * null.
     *
     * @param varName the name of the variable being checked
     * @param o the object to check for nullity
     */
    public static void throwNull(String varName, Object o) {
        if (o == null) {
            throw new IllegalArgumentException(varName + " is null");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified integer is
     * negative (less than 0).
     *
     * @param varName the name of the variable being checked
     * @param x the integer to check for negativity
     */
    public static void throwNegative(String varName, int x) {
        if (x < 0) {
            throw new IllegalArgumentException(varName + " is negative");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified integer is
     * not positive (smaller than or equal to 0).
     *
     * @param varName the name of the variable being checked
     * @param x the integer to check for positive value
     */
    public static void throwNonPositive(String varName, int x) {
        if (x <= 0) {
            throw new IllegalArgumentException(varName + " must be positive");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified integer is
     * out of the specified range.
     *
     * @param varName the name of the variable being checked
     * @param x the integer to check for range validity
     * @param min the minimum valid value (inclusive)
     * @param max the maximum valid value (exclusive)
     */
    public static void throwOutOfRange(String varName, int x, int min, int max) {
        if (x < min || x >= max) {
            throw new IllegalArgumentException(varName + " is out of range [" + min + ", " + max + "]");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified string is
     * null or empty.
     *
     * @param varName the name of the variable being checked
     * @param s the string to check for nullity or emptiness
     */
    public static void throwEmpty(String varName, String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException(varName + " is null or empty");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified collection is
     * null or empty.
     *
     * @param varName the name of the variable being checked
     * @param collection the collection to check for nullity or emptiness
     */
    public static void throwEmpty(String varName, Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(varName + " is null or empty");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified array is null
     * or empty.
     *
     * @param varName the name of the variable being checked
     * @param array the array to check for nullity or emptiness
     * @throws IllegalArgumentException
     */
    public static void throwEmpty(String varName, Object[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(varName + " is null or empty");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified index is out
     * of bounds for the given array.
     *
     * @param varName the name of the variable being checked
     * @param index the index to check for bounds validity
     * @param array the array to check against
     */
    public static void throwInvalidIndex(String varName, int index, Object[] array) {
        if (index < 0 || index >= array.length) {
            throw new IllegalArgumentException(varName + " index " + index + " is out of bounds");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified index is out
     * of bounds for the given list.
     *
     * @param varName the name of the variable being checked
     * @param index the index to check for bounds validity
     * @param list the list to check against
     */
    public static void throwInvalidIndex(String varName, int index, List<?> list) {
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException(varName + " index " + index + " is out of bounds");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified object is not
     * of the expected type.
     *
     * @param varName the name of the variable being checked
     * @param o the object to check for type validity
     * @param clazz the expected class type
     */
    public static void throwInvalidType(String varName, Object o, Class<?> clazz) {
        if (!clazz.isInstance(o)) {
            throw new IllegalArgumentException(varName + " is not of type " + clazz.getName());
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified string's
     * length is not within the specified range.
     *
     * @param varName the name of the variable being checked
     * @param s the string to check for length validity
     * @param minLength the minimum valid length (inclusive)
     * @param maxLength the maximum valid length (inclusive)
     */
    public static void throwInvalidLength(String varName, String s, int minLength, int maxLength) {
        if (s == null || s.length() < minLength || s.length() > maxLength) {
            throw new IllegalArgumentException(varName + " length is out of range [" + minLength + ", " + maxLength + "]");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified array is
     * null, empty, or has a null element.
     *
     * @param varName the name of the variable being checked
     * @param array the array to check for validity
     */
    public static void throwNonEmptyArray(String varName, Object[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(varName + " is null or empty");
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalArgumentException(varName + " is null at index " + i);
            }
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified collection's
     * size is not within the specified range.
     *
     * @param varName the name of the variable being checked
     * @param collection the collection to check for size validity
     * @param minSize the minimum valid size (inclusive)
     * @param maxSize the maximum valid size (inclusive)
     */
    public static void throwInvalidCollectionSize(String varName, Collection<?> collection, int minSize, int maxSize) {
        if (collection == null || collection.size() < minSize || collection.size() > maxSize) {
            throw new IllegalArgumentException(varName + " size is out of range [" + minSize + ", " + maxSize + "]");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified integer value
     * is not one of the expected values.
     *
     * @param varName the name of the variable being checked
     * @param value the integer to check for validity
     * @param validValues the array of valid integer values
     */
    public static void throwInvalidValue(String varName, int value, int... validValues) {
        for (int validValue : validValues) {
            if (value == validValue) {
                return;
            }
        }
        throw new IllegalArgumentException(varName + " is not a valid value");
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified string does
     * not match the specified pattern.
     *
     * @param varName the name of the variable being checked
     * @param s the string to check for pattern validity
     * @param pattern the regex pattern to match against
     */
    public static void throwInvalidPattern(String varName, String s, Pattern pattern) {
        if (s == null || !pattern.matcher(s).matches()) {
            throw new IllegalArgumentException(varName + " does not match the required pattern");
        }
    }
    
}