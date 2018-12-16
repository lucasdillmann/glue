package glue.core.config.propertiesFile;

import glue.config.api.resolver.ConfigurationResolver;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Properties;

/**
 * {@link ConfigurationResolver} implementation backed by simple properties files
 *
 * <p>This class implements the Configuration API {@link ConfigurationResolver} interface. All values provided
 * by this implementation are retrieved from configuration properties available in the claspath. By default the
 * configuration properties are named {@code application.properties}.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-04
 */
public class ConfigurationPropertiesFileConfigurationResolver implements ConfigurationResolver {

    private final Properties properties;
    private final Logger logger;

    /**
     * Constructor with {@link Properties} and {@link Logger} initialization
     *
     * @param properties Properties values to be used
     * @param logger Logger instance
     */
    @Inject
    public ConfigurationPropertiesFileConfigurationResolver(final @ConfigurationProperties Properties properties,
                                                            final Logger logger) {
        this.properties = properties;
        this.logger = logger;
    }

    /**
     * Resolves the configuration value for the provided key
     *
     * @param key Configuration key to be resolved
     * @return Resolved value
     */
    @Override
    public String resolve(final String key) {
        return properties.getProperty(key);
    }
}
