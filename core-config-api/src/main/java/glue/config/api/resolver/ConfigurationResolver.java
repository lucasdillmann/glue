package glue.config.api.resolver;

/**
 * Configuration resolver interface
 *
 * <p>This interface declares the required method that any Configuration Resolver module needs to implement to
 * integrate himself with the Glue Configurtion API.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
public interface ConfigurationResolver {

    /**
     * Resolves the configuration value for the provided key
     *
     * @param key Configuration key to be resolved
     * @return Resolved value
     */
    String resolve(String key);

}
