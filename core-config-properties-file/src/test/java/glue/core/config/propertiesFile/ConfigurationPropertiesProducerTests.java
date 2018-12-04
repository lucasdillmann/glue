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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Test cases for {@link ConfigurationPropertiesProducer}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-03
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationPropertiesProducerTests {

    @Mock
    private Logger logger;

    private ConfigurationPropertiesProducer producer;

    @Before
    public void setup() {
        this.producer = new ConfigurationPropertiesProducer(logger);
    }

    @Test
    public void shouldProduceConfigurationProperties() {
        // execution
        final Properties properties = producer.configurationProperties();

        // validation
        assertNotNull(properties);
    }

    @Test
    public void shouldReadConfigurationFile() {
        // scenario
        final String configKey = "test";
        final String expectedValue = "12354";

        // execution
        final Properties properties = producer.configurationProperties();

        // validation
        assertNotNull(properties);
        assertThat(properties.getProperty(configKey), is(equalTo(expectedValue)));
    }

}
