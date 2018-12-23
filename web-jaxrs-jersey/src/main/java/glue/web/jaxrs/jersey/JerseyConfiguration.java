package glue.web.jaxrs.jersey;

import glue.config.api.annotation.ConfigurationInterface;
import glue.config.api.annotation.ConfigurationProperty;

/**
 * Jersey configuration properties
 *
 * <p>This class works with Core Config API and aims to provide access to Jersey related configuration options.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@ConfigurationInterface(prefix = "glue.web.jaxrs.")
public interface JerseyConfiguration {

    @ConfigurationProperty(key = "contextPath", defaultValue = "/*")
    String getContextPath();

    @ConfigurationProperty(key = "servletName", defaultValue = "Jersey")
    String getServletName();
}
