package glue.config.api.extension;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ConfigurationMetadataTests.class,
        ConfigurationProxyFactoryTests.class,
        ConfigurationProxyHandlerTests.class,
        ConfigurationResolverBridgeTests.class,
        ConfigurationTranslatorBridgeTests.class,
        DefaultConfigurationTranslatorTests.class
})
public class UnitTestSuite {
}
