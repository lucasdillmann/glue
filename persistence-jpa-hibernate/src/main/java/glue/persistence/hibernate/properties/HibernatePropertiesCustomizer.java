package glue.persistence.hibernate.properties;

import java.util.Properties;

/**
 * Hibernate configuration properties customizer interface
 *
 * <p>This interface allows to any project customize the configuration properties that will be used to create
 * {@link javax.persistence.EntityManager} instances with Hibernate ORM. All implementation will be called whenever
 * the persistence unit is created.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-12
 */
public interface HibernatePropertiesCustomizer {

    /**
     * Executes the customization of the Hibernate ORM {@link Properties}
     *
     * @param properties Hibernate properties
     */
    void customize(Properties properties);
}
