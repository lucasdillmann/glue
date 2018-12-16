package glue.core;

import glue.core.util.CdiUtilsTests;
import glue.core.util.ExceptionUtilsTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ExceptionUtilsTests.class,
        LifecycleControllerTests.class,
        CdiUtilsTests.class
})
public class UnitTestSuite {
}
