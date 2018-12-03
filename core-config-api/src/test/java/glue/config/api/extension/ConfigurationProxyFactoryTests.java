package glue.config.api.extension;

import glue.config.api.exception.ConfigurationException;
import glue.config.api.extension.artifacts.ConfigurationProxyFactoryArtifact;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Test cases for {@link ConfigurationProxyFactory}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationProxyFactoryTests {

    @Rule
    public  ExpectedException expectedException = ExpectedException.none();
    @Mock
    private ConfigurationProxyHandler proxyHandler;
    @Mock
    private Logger logger;
    private ConfigurationProxyFactory factory;

    @Before
    public void setup() {
        this.factory = new ConfigurationProxyFactory(proxyHandler, logger);
    }

    @Test
    public void shouldProduceConfigurationProxies() {
        // scenario
        final Class<ConfigurationProxyFactoryArtifact.Valid> configurationInterface = ConfigurationProxyFactoryArtifact.Valid.class;

        // execution
        final ConfigurationProxyFactoryArtifact.Valid producedInstance = factory.build(configurationInterface);

        // validation
        assertNotNull(producedInstance);
        assertThat(producedInstance, is(instanceOf(configurationInterface)));

    }

    @Test
    public void shouldThrowExceptionWhenAnnotationIsMissing() {
        // scenario
        final Class<ConfigurationProxyFactoryArtifact.WithoutAnnotation> configurationInterface
                = ConfigurationProxyFactoryArtifact.WithoutAnnotation.class;
        final String expectedMessage = "Provided interface don't have the required ConfigurationInteface annotation: " + configurationInterface.getName();
        expectedException.expect(ConfigurationException.class);
        expectedException.expectMessage(equalTo(expectedMessage));

        // execution
        factory.build(configurationInterface);
    }

    @Test
    public void shouldThrowExceptionWhenIsntAnInterface() {
        // scenario
        final Class<ConfigurationProxyFactoryArtifact.NotAInterface> configurationInterface
                = ConfigurationProxyFactoryArtifact.NotAInterface.class;
        final String expectedMessage = "Provided class isn't a interface: " + configurationInterface.getName();
        expectedException.expect(ConfigurationException.class);
        expectedException.expectMessage(equalTo(expectedMessage));

        // execution
        factory.build(configurationInterface);
    }

}
