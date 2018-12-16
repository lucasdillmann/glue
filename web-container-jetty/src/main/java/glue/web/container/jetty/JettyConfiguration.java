package glue.web.container.jetty;

import glue.config.api.annotation.ConfigurationInterface;
import glue.config.api.annotation.ConfigurationProperty;

/**
 * Configuration interface for Jetty Web Container
 *
 * <p>This interface holds configuration definitions for the Jetty implementation of the Web Container API.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-03
 */
@ConfigurationInterface(prefix = "glue.webcontainer.jetty.")
public interface JettyConfiguration {

    /**
     * Returns the TCP port number where the Jetty should listen. Defaults to "8080".
     *
     * @return TCP port where Jetty should listen
     */
    @ConfigurationProperty(key = "port", defaultValue = "8080")
    Integer getPort();

}
