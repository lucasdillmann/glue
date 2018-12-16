package glue.config.api.extension;

import glue.config.api.exception.ConfigurationException;
import glue.config.api.translator.ConfigurationContainerTranslator;
import glue.core.util.CdiUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Configuration utility class for container objects
 *
 * <p>This class provides utility methods for configuration API to enable support for container objects, like {@link Optional}.
 * Its methods allows simple detection when is a container and simple detection of the target type of the values
 * that the container holds.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-09
 */
@Singleton
class ConfigurationContainerFacade {

    private final CdiUtils cdiUtils;
    private final Logger logger;

    /**
     * Package protected constructor with {@link CdiUtils} and {@link Logger} initialization
     *
     * @param cdiUtils CDI utility
     * @param logger   Logger instance
     */
    @Inject
    ConfigurationContainerFacade(final CdiUtils cdiUtils,
                                 final Logger logger) {
        this.cdiUtils = cdiUtils;
        this.logger = logger;
    }

    /**
     * Checks if the return type is a container by looking if any {@link ConfigurationContainerTranslator} implementation
     * if available for it
     *
     * @param returnType Return type to check
     * @param <T>        Generic container type
     * @return Result of the validation. True means that a {@link ConfigurationContainerTranslator} is available.
     */
    <T> boolean isAContainer(final Class<T> returnType) {
        logger.debug("Checking if {} is a container by looking for available ConfigurationContainerTranslator", returnType.getName());
        return getTranslator(returnType).isPresent();
    }

    /**
     * Retrieves a compatible instance of {@link ConfigurationContainerTranslator} for the provided return type
     *
     * @param returnType Container return type
     * @param <T> Generic container return type
     * @return Translator instance
     */
    private <T> Optional<ConfigurationContainerTranslator<T>> getTranslator(final Class<T> returnType) {
        return cdiUtils.getTypedBean(ConfigurationContainerTranslator.class, returnType);
    }

    /**
     * Invokes the {@link ConfigurationContainerTranslator} to translate the return type to the actual configuration value
     * type
     *
     * <p>This method invokes the {@link ConfigurationContainerTranslator} to return the actual configuration type
     * that should be used by configuration API. For example, if the return is {@code Optional<Example>} this method
     * needs to return {@code Example}.</p>
     *
     * @param containerType Container type
     * @param returnType    Generic return type from {@link Method#getGenericReturnType()}
     * @param <T>           Generic container type
     * @return Target configuration value type
     */
    <T> Class<?> getTargetConfigurationValueType(final Class<T> containerType, final Type returnType) {
        logger.debug("Checking {} container value type for {}", containerType.getName(), returnType);
        return getTranslator(containerType)
                .orElseThrow(() -> new RuntimeException("Unknown error found"))
                .getTargetType(returnType);
    }

    /**
     * Translates the configuration value to the requested target container type
     *
     * <p>This methods translates the requested configuration value into the target container type. When available,
     * compatible {@link ConfigurationContainerTranslator} instances will be used. If no implementation is found this
     * method will throw an Exception.</p>
     *
     * @param value         Value to be translated
     * @param containerType Target type of the translation
     * @param <T>           Generic type of the container
     * @param <V>           Generic type of the configuration value
     * @return Translated container object
     * @throws ConfigurationException when multiple or no {@link ConfigurationContainerTranslator} compatible implementations
     *                                is found
     */
    <T, V> T translate(final V value, final Class<T> containerType) {
        logger.debug("Translating configuration value '{}' to container of type {}", value, containerType.getName());

        return getTranslator(containerType)
                .orElseThrow(() -> new RuntimeException("Unknown error found"))
                .translate(value);
    }

}
