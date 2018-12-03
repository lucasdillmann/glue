package glue.config.api.annotation;

import java.lang.annotation.*;

/**
 * Configuration annotation for configuration interfaces
 *
 * <p>This annotation enables interfaces to work as dynamic configuration gateways, providing useful informations
 * to the Config API like the prefix of the keys.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ConfigurationInterface {

    /**
     * Defines the prefix of the configuration keys for the configuration interface
     *
     * @return Prefix of the configuration keys
     */
    String prefix() default "";

}
