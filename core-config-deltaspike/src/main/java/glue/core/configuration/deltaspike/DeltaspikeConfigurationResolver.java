package glue.core.configuration.deltaspike;

import glue.config.api.resolver.ConfigurationResolver;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

/**
 * Apache Deltaspike implementation for {@link ConfigurationResolver} API
 *
 * <p>This class implements the Configuration API resolver interface using Apache Deltaspike as the provider,
 * allowing the Glue configuration resolution using the power of the Deltaspike APIs.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@Singleton
public class DeltaspikeConfigurationResolver implements ConfigurationResolver {

    private final Logger logger;

    /**
     * Constructor with {@link Logger} initialization
     *
     * @param logger Logger
     */
    @Inject
    public DeltaspikeConfigurationResolver(Logger logger) {
        this.logger = logger;
    }

    /**
     * Resolves the configuration value for the provided key
     *
     * @param key Configuration key to be resolved
     * @return Resolved value
     */
    @Override
    public String resolve(String key) {
        Objects.requireNonNull(key);
        logger.debug("Resolving configuration '{}' using Apache Deltaspike", key);

        return ConfigResolver.getPropertyValue(key);
    }
}
