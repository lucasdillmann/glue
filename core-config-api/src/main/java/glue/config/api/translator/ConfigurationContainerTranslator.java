package glue.config.api.translator;

import java.lang.reflect.Type;

/**
 * Definition interface for container translators for configuration values
 *
 * <p>This interface defines an API to allow translation of configuration values to container values.
 * For example, if a configuration class uses {@link java.util.Optional} as the return type, an implementation
 * of this interface that supports {@link java.util.Optional} will be called to translate the resolved configuration
 * value to it.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-09
 */
public interface ConfigurationContainerTranslator<T> {

    /**
     * Translates a configuration value to the supported container object
     *
     * @param value Configuration value to be translated
     * @return Configuration value under supported container
     */
    <V> T translate(V value);

    /**
     * Returns the target type that the container holds
     *
     * @param genericType Generic type to be returned
     * @return Class of the objects that the container can hold
     */
    Class<?> getTargetType(final Type genericType);

}
