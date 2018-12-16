package glue.persistence.hibernate.configuration;

import glue.config.api.annotation.ConfigurationInterface;
import glue.config.api.annotation.ConfigurationProperty;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;

/**
 * Configuration properties for Hibernate implementation of the Persistence API module using JPA API
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-07
 */
@ConfigurationInterface(prefix = "glue.persistence.jpa.")
public interface HibernateConfiguration {

    /**
     * Returns the database JDBC connection URL
     *
     * @return JDBC connection URL
     */
    @ConfigurationProperty(key = "url", required = true)
    String getUrl();

    /**
     * Returns the database username for authentication if required
     *
     * @return Database username
     */
    @ConfigurationProperty(key = "username")
    String getUsername();

    /**
     * Returns the default database schema.
     *
     * @return Database username
     */
    @ConfigurationProperty(key = "schema")
    String getSchema();

    /**
     * Returns the database password for authentication if required
     *
     * @return Database password
     */
    @ConfigurationProperty(key = "password")
    String getPassword();

    /**
     * Returns the database JDBC driver class
     *
     * @return JDBC driver class name
     */
    @ConfigurationProperty(key = "driverClass", required = true)
    String getDriverClass();

    /**
     * Returns the shared cache mode to be used. Defaults to unspecified.
     *
     * @return Shared cache mode
     */
    @ConfigurationProperty(key = "sharedCacheMode", defaultValue = "UNSPECIFIED")
    SharedCacheMode getSharedCacheMode();

    /**
     * Returns the validation mode to be used. Defaults to auto.
     *
     * @return Validation mode
     */
    @ConfigurationProperty(key = "validationMode", defaultValue = "AUTO")
    ValidationMode getValidationMode();

}
