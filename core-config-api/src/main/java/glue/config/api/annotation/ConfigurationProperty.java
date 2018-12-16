package glue.config.api.annotation;

import java.lang.annotation.*;

/**
 * Configuration annotation for configuration methods
 *
 * <p>This annotation enables methods from configuration interfaces to resolve configuration values. This
 * annotation provides useful informations like the key to be used and the default value.</p>
 *
 * <p>Use of this annotation is optional. When missing, the config API will use the method name as the key (considering
 * the Java Bean pattern).</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ConfigurationProperty {

    String NULL = "configApi.annotation.configurationProperty.nullMarker";

    /**
     * Defines the configuration key to be used. Values provided by this will be used alongside with prefix
     * defined in {@link ConfigurationInterface}.
     *
     * @return Configuration key
     */
    String key();

    /**
     * Defines the default value when no configuration is found from resolver. Defaults to null.
     *
     * @return Default value.
     */
    String defaultValue() default NULL;

    /**
     * Defines if the configuration value is required and an exception should be thrown when null value is found.
     *
     * @return Flag for required configuration value
     */
    boolean required() default  false;

}
