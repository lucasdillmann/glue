package glue.core.config.propertiesFile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Test cases for {@link ConfigurationPropertiesFileConfigurationResolver}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-04
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationPropertiesFileConfigurationResolverTests {

    @Mock
    private Logger logger;
    @Mock
    private Properties properties;

    private ConfigurationPropertiesFileConfigurationResolver resolver;

    @Before
    public void setup() {
        this.resolver = new ConfigurationPropertiesFileConfigurationResolver(properties, logger);
    }

    @Test
    public void shouldResolveConfigurationValues() {
        // scenario
        final String configKey = "test";
        final String expectedValue = "12354";
        doReturn(expectedValue).when(properties).getProperty(configKey);

        // execution
        final String actualValue = resolver.resolve(configKey);

        // validation
        assertThat(actualValue, is(equalTo(expectedValue)));
    }

}
