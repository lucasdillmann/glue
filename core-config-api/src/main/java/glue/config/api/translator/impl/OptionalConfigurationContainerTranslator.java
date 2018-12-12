package glue.config.api.translator.impl;

import glue.config.api.translator.ConfigurationContainerTranslator;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * {@link ConfigurationContainerTranslator} implementation for {@link Optional}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-09
 */
@Default
public class OptionalConfigurationContainerTranslator implements ConfigurationContainerTranslator<Optional> {

    private final Logger logger;

    /**
     * Constructor with {@link Logger} initialization
     *
     * @param logger Logger
     */
    @Inject
    public OptionalConfigurationContainerTranslator(final Logger logger) {
        this.logger = logger;
    }

    /**
     * Translates a configuration value to the supported container object
     *
     * @param value Configuration value to be translated
     * @return Configuration value under supported container
     */
    @Override
    public <V> Optional<V> translate(final V value) {
        logger.debug("Translating '{}' to Optional container", value);
        return Optional.ofNullable(value);
    }

    /**
     * Returns the target type that the container holds
     *
     * @param genericType Generic type to be returned
     * @return Class of the objects that the container can hold
     */
    @Override
    public Class<?> getTargetType(final Type genericType) {
        if (!(genericType instanceof ParameterizedType))
            throw new IllegalArgumentException("Provided generic type isn't a parameterized type");

        final ParameterizedType parameterizedType = (ParameterizedType) genericType;
        if (!Optional.class.equals(parameterizedType.getOwnerType()))
            throw new IllegalArgumentException("Provided generic type isn't a Optional based type");

        if (parameterizedType.getActualTypeArguments() == null || parameterizedType.getActualTypeArguments().length < 1)
            return Object.class;

        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }
}
