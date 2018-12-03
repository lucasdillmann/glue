package glue.config.api.extension;

import glue.config.api.resolver.ConfigurationResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link ConfigurationResolverBridge}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationResolverBridgeTests {

    @Mock
    private ConfigurationResolver resolver;
    @Mock
    private Logger logger;

    private ConfigurationResolverBridge bridge;

    @Before
    public void setup() {
        this.bridge = new ConfigurationResolverBridge(resolver, logger);
    }

    @Test
    public void shouldInvokeResolver() {
        // scenario
        final String expectedValue = Double.toString(new Random().nextDouble());
        final String key = "testKey";

        doReturn(expectedValue).when(resolver).resolve(key);

        // execution
        final String actualValue = bridge.resolve(key, null);

        // validation
        verify(resolver, times(1)).resolve(key);
        assertThat(actualValue, is(expectedValue));
    }

    @Test
    public void shouldReturnDefaultValueWhenResolverReturnsNull() {
        // scenario
        doReturn(null).when(resolver).resolve(any());
        final String randomKey = Double.toString(new Random().nextDouble());
        final String expectedValue = Double.toString(new Random().nextDouble());

        // execution
        final String actualValue = bridge.resolve(randomKey, expectedValue);

        // validation
        verify(resolver, times(1)).resolve(randomKey);
        assertThat(actualValue, is(expectedValue));

    }

    @Test
    public void shouldCreateLogEvent() {
        // scenario
        final String randomKey = Double.toString(new Random().nextDouble());
        final String randomDefaultValue = Double.toString(new Random().nextDouble());

        // execution
        bridge.resolve(randomKey, randomDefaultValue);

        // validation
        verify(logger, times(1))
                .debug("Resolving configuration value for '{}' with default '{}'", randomKey, randomDefaultValue);

    }

}
