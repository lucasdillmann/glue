package glue.core.configuration.deltaspike;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test cases for {@link DeltaspikeConfigurationResolver}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@RunWith(MockitoJUnitRunner.class)
public class DeltaspikeConfigurationResolverTests {

    @Mock
    private Logger logger;

    private DeltaspikeConfigurationResolver resolver;

    @Before
    public void setup() {
        this.resolver = new DeltaspikeConfigurationResolver(logger);
    }

    @Test
    public void shouldCreateLogEvent() {
        // scenario
        final String key = Double.toString(new Random().nextDouble());

        // execution
        resolver.resolve(key);

        // validation
        verify(logger, times(1)).debug("Resolving configuration '{}' using Apache Deltaspike", key);
    }

    @Test
    public void shouldResolveConfigurationValues() {
        // scenario
        final String key = "testValue";
        final String expectedValue = "1234";

        // execution
        final String actualValue = resolver.resolve(key);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(expectedValue));
    }

}
