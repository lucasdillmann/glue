package glue.webcontainer.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        WebContainerLifecycleTests.class,
        WebContainerModuleDiscoveryTests.class
})
public class UnitTestSuite {
}
