package glue.core.config.propertiesFile;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration properties qualifier for CDI injections
 *
 * <p>This qualifier enables the injection of configuration {@link java.util.Properties} instances
 * using the default provider or customized ones. This qualifier exists primarily to avoid conflict with any other
 * injection of the same type.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-03
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface ConfigurationProperties {
}
