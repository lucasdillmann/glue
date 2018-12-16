package glue.core.config.propertiesFile;

import glue.core.util.ExceptionUtils;
import org.slf4j.Logger;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Configuration {@link Properties} producer
 *
 * <p>This class produces {@link Properties} instances from configuration file for all injections using
 * {@link ConfigurationProperties} qualifier.</p>
 *
 * <p>This class reads all values from all {@code application.properties} files available in the classpath and
 * put them in the produced instance. When no file is found an empty instance will be returned instead.
 * If multiple files uses the same configuraton key this class don't guarantee any precedence (in another words, it dont
 * guarantee that configuration from one file will be loaded instead of another).</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-03
 */
public class ConfigurationPropertiesProducer {

    private static final String APPLICATION_CONFIGURATION_FILE_PATH = "application.properties";
    private final Logger logger;

    /**
     * Constructor with {@link Logger} initialization
     *
     * @param logger Logger
     */
    @Inject
    public ConfigurationPropertiesProducer(Logger logger) {
        this.logger = logger;
    }

    /**
     * Produces a {@link Properties} instance with configuration properties
     *
     * @return Configuration Properties
     */
    @Produces
    @ConfigurationProperties
    @Singleton
    public Properties configurationProperties() {
        logger.debug("Producing configuration properties");
        final Properties properties = new Properties();
        getAllAvailableProperties().forEach(properties::putAll);
        properties.putAll(getProjectProperties());
        return properties;
    }

    /**
     * Loads all {@code application.properties} files available in the classpath into {@link Properties} objects
     *
     * @return Collection of loaded properties
     */
    private List<Properties> getAllAvailableProperties() {
        logger.debug("Looking for all configuration files available in the classpath");
        try {
            final Enumeration<URL> configurationFiles = getClass()
                    .getClassLoader()
                    .getResources(APPLICATION_CONFIGURATION_FILE_PATH);

            final URL mainConfigurationFile = getClass().getResource("/" + APPLICATION_CONFIGURATION_FILE_PATH);

            return Collections
                    .list(configurationFiles)
                    .stream()
                    .filter(element -> !element.equals(mainConfigurationFile))
                    .map(this::loadConfigurationFile)
                    .collect(Collectors.toList());
        } catch (final Exception ex) {
            new ExceptionUtils().rethrowAsUnchecked(ex);
            return null;
        }
    }

    /**
     * Loads the configuration {@link Properties} from the given {@link URL}
     *
     * @param configurationFileUrl Configuration file URL to be loaded
     * @return Loaded properties
     */
    private Properties loadConfigurationFile(final URL configurationFileUrl) {
        logger.info("Loading configuration properties from {}", configurationFileUrl.getPath());
        try {
            final Properties properties = new Properties();
            properties.load(configurationFileUrl.openStream());
            return properties;
        } catch (final Exception ex) {
            new ExceptionUtils().rethrowAsUnchecked(ex);
            return null;
        }
    }

    /**
     * Loads the configuration properties from main JAR where the user application (project) code resides
     *
     * <p>This method looks for {@code application.properties} file in the main application JAR. When found
     * it will be loaded into a {@link Properties} object.</p>
     *
     * @return Loaded Properties from configuration file
     */
    private Properties getProjectProperties() {
        logger.debug("Loading configuration properties using main application configuration file");
        final URL configurationFile = getClass().getResource("/" + APPLICATION_CONFIGURATION_FILE_PATH);
        if (configurationFile == null) {
            logger.warn("No configuration file found for main application. Using Glue defaults instead.");
            return new Properties();
        }

        return loadConfigurationFile(configurationFile);
    }


}
