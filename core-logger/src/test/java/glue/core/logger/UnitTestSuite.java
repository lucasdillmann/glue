package glue.core.logger;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LoggerProviderTests.class,
        LoggerModuleDiscoveryTests.class
})
public class UnitTestSuite {
}