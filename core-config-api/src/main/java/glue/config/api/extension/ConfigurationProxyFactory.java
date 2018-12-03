package glue.config.api.extension;

import glue.config.api.annotation.ConfigurationInterface;
import glue.config.api.exception.ConfigurationException;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * Proxy factory class for configuration interfaces
 *
 * <p>This class produces dynamic Java proxies for configuration interfaces</p>
 */
class ConfigurationProxyFactory {

    private final ConfigurationProxyHandler handler;
    private final Logger logger;

    /**
     * Constructor with {@link ConfigurationProxyHandler} and {@link Logger} initialization
     *
     * @param handler Java proxy handler instance to be used
     * @param logger Logger instance
     */
    @Inject
    public ConfigurationProxyFactory(final ConfigurationProxyHandler handler,
                                     final Logger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    /**
     * Produces a dynamic proxy for the given interface using configuration API
     *
     * @param configurationInterface Configuration interface to create the proxy for
     * @param <T> Generic configuration interface type
     * @return Proxy instance
     * @throws glue.config.api.exception.ConfigurationException when the supplied class isn't a interface or don't
     * contains {@link glue.config.api.annotation.ConfigurationInterface} annotation
     */
    <T> T build(final Class<T> configurationInterface) {
        Objects.requireNonNull(configurationInterface);
        validateInterface(configurationInterface);

        logger.debug("Producing configuration proxy for {}", configurationInterface.getName());

        return (T) Proxy.newProxyInstance(
                configurationInterface.getClassLoader(),
                new Class[]{configurationInterface},
                handler
        );
    }

    /**
     * Validates if the given class is a valid configuration interface
     *
     * @param clazz Class to be validated
     */
    private void validateInterface(final Class<?> clazz) {
        logger.debug("Checking if {} is a valid configuration interface", clazz.getName());

        if (!clazz.isInterface())
            throw new ConfigurationException("Provided class isn't a interface: " + clazz.getName());

        if (clazz.getAnnotation(ConfigurationInterface.class) == null)
            throw new ConfigurationException("Provided interface don't have the required ConfigurationInteface annotation: " + clazz.getName());

    }
}
