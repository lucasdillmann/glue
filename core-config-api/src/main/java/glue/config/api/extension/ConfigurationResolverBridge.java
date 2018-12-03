package glue.config.api.extension;

import glue.config.api.resolver.ConfigurationResolver;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Configuration resolution bridge
 *
 * <p>This class acts as a bridge between the {@link ConfigurationResolver} implementation module and configuration
 * API module. It main goal is to call the implementation and, when no value is returned, return the default
 * configuration value instead.</p>
 */
@Singleton
class ConfigurationResolverBridge {

    private final ConfigurationResolver resolver;
    private final Logger logger;

    /**
     * Default constructor with {@link ConfigurationResolver} and {@link Logger} initialization
     *
     * @param resolver Configuration resolver
     * @param logger Logger
     */
    @Inject
    ConfigurationResolverBridge(final ConfigurationResolver resolver,
                                final Logger logger) {
        this.resolver = resolver;
        this.logger = logger;
    }

    /**
     * Forwards the configuration resolution, returning the default value when no value can be retrieved from the
     * resolver
     *
     * @param configurationKey Configuration key to resolve the value
     * @param defaultValue Default value to be returned when resolution fails
     * @return Configuration value
     */
    String resolve(final String configurationKey,
                   final String defaultValue) {
        logger.debug("Resolving configuration value for '{}' with default '{}'", configurationKey, defaultValue);

        return Optional
                .ofNullable(resolver.resolve(configurationKey))
                .orElse(defaultValue);
    }
}
