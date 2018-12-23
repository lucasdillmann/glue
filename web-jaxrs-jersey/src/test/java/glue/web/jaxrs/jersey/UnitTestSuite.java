package glue.web.jaxrs.jersey;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        JerseyServletProviderTests.class,
        JerseyServletConfigTests.class,
        JerseyJaxRsProviderTests.class
})
public class UnitTestSuite {
}
