package glue.persistence.datasource.hikaricp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        HikariCpDataSourceProviderTests.class,
        HikariCpFactoryTests.class
})
public class UnitTestSuite {
}
