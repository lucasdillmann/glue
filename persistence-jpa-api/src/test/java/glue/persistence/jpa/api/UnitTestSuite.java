package glue.persistence.jpa.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EntityManagerProviderTests.class,
        PersistenceContextTests.class,
        PersistenceJpaApiModuleTests.class
})
public class UnitTestSuite {
}
