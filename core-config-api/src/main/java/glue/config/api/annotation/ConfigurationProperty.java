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

    String key();
    String defaultValue() default NULL;

}
