package glue.config.api.extension;

import glue.config.api.annotation.ConfigurationInterface;
import glue.config.api.annotation.ConfigurationProperty;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * Configuration metadata utility class
 *
 * <p>This class extracts configuration metadata from methods of configuration interfaces. The metadata contains the
 * required information the configuration API needs to resolve configuration values, like the key and default value.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
class ConfigurationMetadata {

    private final Method method;

    /**
     * Package protected contructor with the target method initialization
     *
     * @param method Method to extract the metadata from
     */
    ConfigurationMetadata(final Method method) {
        Objects.requireNonNull(method);
        this.method = method;
    }

    /**
     * Returns the configuration key
     *
     * <p>The key will be extracted from {@link ConfigurationInterface} and {@link ConfigurationProperty} annotations
     * when present or method name otherwise (following Java Beans pattern).</p>
     *
     * @return Configuration key
     */
    String getKey() {
        final String propertyKey = getConfigurationPropertyAnnotation()
                .map(ConfigurationProperty::key)
                .orElseGet(() -> getPropertyKeyFromMethodName());

        return getConfigurationPrefix() + propertyKey;
    }

    /**
     * Retrieves the key using method name
     *
     * @return Configuration key
     */
    private String getPropertyKeyFromMethodName() {
        String methodName = method.getName();
        if (methodName.startsWith("is"))
            methodName = methodName.substring(2);

        else if (methodName.startsWith("get"))
            methodName = methodName.substring(3);

        if (methodName.length() == 0)
            throw new IllegalArgumentException(
                    "Error retrieving configuration key from method " + method.getName() + " of interface " +
                            method.getDeclaringClass().getName() + ". Name isn't a valid Java Bean name."
            );

        return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
    }

    /**
     * Retrieves the configuration key prefix using {@link ConfigurationInterface} annotation
     *
     * @return Configuration key prefix
     */
    private String getConfigurationPrefix() {
        final ConfigurationInterface configurationInterface = method
                .getDeclaringClass()
                .getAnnotation(ConfigurationInterface.class);

        return Optional
                .ofNullable(configurationInterface)
                .map(ConfigurationInterface::prefix)
                .orElse("");
    }

    /**
     * Returns the target configuration type using the method return type
     *
     * @return Target configuration type
     */
    Class<?> getTargetType() {
        return method.getReturnType();
    }

    /**
     * Returns the default configuration value using {@link ConfigurationProperty} annotation
     *
     * @return Default configuration value
     */
    String getDefaultValue() {
        return getConfigurationPropertyAnnotation()
                .map(ConfigurationProperty::defaultValue)
                .orElse(null);
    }

    /**
     * Returns {@link ConfigurationProperty} annotation instance for current method
     *
     * @return Configuration property annotation instance
     */
    private Optional<ConfigurationProperty> getConfigurationPropertyAnnotation() {
        return Optional.ofNullable(method.getAnnotation(ConfigurationProperty.class));
    }

}
