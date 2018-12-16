package glue.persistence.hibernate;

import glue.persistence.hibernate.properties.HibernatePropertiesCustomizer;
import org.slf4j.Logger;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Hibernate persistence unit {@link Properties} factory
 *
 * <p>This class is able to produce {@link Properties} instances for use by Hibernate ORM. All produced instances
 * are customized by {@link HibernatePropertiesCustomizer} implementations.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-12
 */
@Singleton
class HibernatePropertiesFactory {

    private final List<HibernatePropertiesCustomizer> customizers;
    private final Logger logger;

    /**
     * Constructor with CDI {@link Instance} for {@link HibernatePropertiesCustomizer} and {@link Logger} initialization
     *
     * @param customizers Customizers CDI gateway object
     * @param logger Logger
     */
    @Inject
    public HibernatePropertiesFactory(final @Any Instance<HibernatePropertiesCustomizer> customizers,
                                      final Logger logger) {
        this.customizers = StreamSupport.stream(customizers.spliterator(), false).collect(Collectors.toList());
        this.logger = logger;
    }

    /**
     * Creates and returns an {@link Properties} object suitable to be used in Hibernate ORM
     *
     * @return Properties
     */
    Properties build() {
        final Properties properties = new Properties();
        customizers.forEach(customizer -> runCustomization(customizer, properties));

        return properties;
    }

    /**
     * Invokes the provided customizer to handle the {@link Properties} being created
     *
     * @param customizer Customizer
     * @param properties Configuration properties
     */
    private void runCustomization(final HibernatePropertiesCustomizer customizer, final Properties properties) {
        logger.debug("Invoking Hibernate properties customizer {}", customizer.getClass().getName());
        customizer.customize(properties);
    }
}
