package glue.persistence.hibernate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        HibernateDataSourceFactoryTests.class,
        HibernatePropertiesFactoryTests.class,
        HibernateJpaProviderTests.class
})
public class UnitTestSuite {
}
