package glue.test.config;

import glue.persistence.hibernate.properties.HibernatePropertiesCustomizer;

import javax.enterprise.inject.Default;
import java.util.Properties;

@Default
public class HibernateConfigCustomizer implements HibernatePropertiesCustomizer {

    /**
     * Executes the customization of the Hibernate ORM {@link Properties}
     *
     * @param properties Hibernate properties
     */
    @Override
    public void customize(final Properties properties) {
        // Enables automatic DDL generation and execution
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
    }
}
