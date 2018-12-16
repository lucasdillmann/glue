package glue.web.container.jetty;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        JettyServerProducerTests.class,
        JettyWebContainerTests.class
})
public class UnitTestSuite {
}
