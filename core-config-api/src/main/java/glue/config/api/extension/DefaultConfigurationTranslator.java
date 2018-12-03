package glue.config.api.extension;

import glue.config.api.exception.ConfigurationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Default configuration translation mechanism
 *
 * <p>This class acts as a default configuration translation mechanism. It main goal is to enable automatic
 * translation without the need for a {@link glue.config.api.translator.ConfigurationValueTranslator} implementation
 * for every existent type.</p>
 *
 * <p>Translation here is done using static methods or constructors that accepts String as input. {@code valueOf},
 * {@code fromString} and {@code parseString} are examples of methods that this class tries to find and use.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 * @param <T> Generic target type of the conversion
 */
class DefaultConfigurationTranslator<T> {

    private final Class<T> targetType;

    /**
     * Private constructor with target type initialization
     *
     * @param targetType Target type of the conversion
     */
    private DefaultConfigurationTranslator(Class<T> targetType) {
        this.targetType = targetType;
    }

    /**
     * Static constructor method
     *
     * <p>This method produces types instances for this class, allowing simple and direct use of the Java Generic API
     * with it.</p>
     *
     * @param targetType Target type of the conversion
     * @param <T> Generic type of the target type
     * @return Class instance for the target type
     */
    static <T> DefaultConfigurationTranslator<T> forType(final Class<T> targetType) {
        return new DefaultConfigurationTranslator<>(targetType);
    }

    /**
     * Executes the translation of the value using the default translation mechanism
     *
     * @param value Value to be translated
     * @return Translated value
     * @throws ConfigurationException when translation can't be done
     */
    T translate(final String value) {

        if (targetType.isEnum())
            return translateEnum(value);

        if (containsConstructor())
            return invokeConstructor(value);

        if (containsMethod("valueOf"))
            return invokeMethod("valueOf", value);

        if (containsMethod("fromString"))
            return invokeMethod("fromString", value);

        if (containsMethod("parse"))
            return invokeMethod("parse", value);

        if (containsMethod("parseString"))
            return invokeMethod("parseString", value);

        throw new ConfigurationException(
                "Configuration value '" + value + "' can't be automatically translated to " +
                        targetType.getName() + " using default translation mechanism. Please implement a custom " +
                        "ConfigurationValueTranslator for the type to solve this."
        );

    }

    /**
     * Invokes the request method using Reflection API
     *
     * @param methodName Method name to be invoked
     * @param value Value to be sent as argument to the method
     * @return Returned value by the method. Represents the translated value.
     */
    private T invokeMethod(final String methodName, final String value) {
        try {
            return (T) targetType.getMethod(methodName, String.class).invoke(null, value);
        } catch (final Exception ex) {
            throw new ConfigurationException(
                    "Error invoking method " + methodName + " from " + targetType.getName() +
                            " to translate configuration value '" + value + "' using default translation mechanism"
            );
        }
    }

    /**
     * Invokes the request constructor using Reflection API
     *
     * @param value Value to be sent as argument to the method
     * @return Returned value by the constructor. Represents the translated value.
     */
    private T invokeConstructor(final String value) {
        try {
            return (T) targetType.getConstructor(String.class).newInstance(value);
        } catch (final Exception ex) {
            throw new ConfigurationException(
                    "Error invoking constructor from " + targetType.getName() +
                            " to translate configuration value '" + value + "' using default translation mechanism"
            );
        }
    }

    /**
     * Checks if the target type has a method with the requested name that accepts {@link String} as parameter
     *
     * @param methodName Method name to be checked
     * @return Result of the check. True means that a compatible method was found.
     */
    private boolean containsMethod(final String methodName) {
        try {
            final Method method = targetType.getMethod(methodName, String.class);
            return method != null
                    && Modifier.isStatic(method.getModifiers())
                    && Modifier.isPublic(method.getModifiers());

        } catch (final NoSuchMethodException ex) {
            return false;
        }
    }

    /**
     * Checks if a constructor is available in the target type that accepts {@link String} as parameter
     *
     * @return Result of the check. True means that a compatible constructor was found.
     */
    private boolean containsConstructor() {
        try {
            final Constructor<T> constructor = targetType.getConstructor(String.class);
            return constructor != null
                    && Modifier.isPublic(constructor.getModifiers());

        } catch (final NoSuchMethodException ex) {
            return false;
        }
    }

    /**
     * Executes the translation of enum values
     *
     * @param value Value to be translated
     * @param <E> Generic enumeration type
     * @return Translated value
     */
    private <E extends Enum<E>> T translateEnum(final String value) {
        final Class<E> enumType = (Class<E>) targetType;
        final E enumValue = Arrays.stream(enumType.getEnumConstants())
                .filter(constant -> constant.name().equalsIgnoreCase(value))
                .findAny()
                .orElseThrow(() -> new ConfigurationException("No enum constant " + value + " for " + targetType.getName()));

        return (T) enumValue;
    }
}
