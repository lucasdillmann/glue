package glue.config.api.extension;

import glue.config.api.translator.ConfigurationValueTranslator;
import glue.core.util.CdiUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link ConfigurationValueTranslatorBridge}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationValueTranslatorBridgeTests {

    @Mock
    private CdiUtils cdiUtils;
    @Mock
    private Logger logger;

    private ConfigurationValueTranslatorBridge translator;

    @Before
    public void setup() {
        this.translator = new ConfigurationValueTranslatorBridge(cdiUtils, logger);
    }


    @Test
    public void shouldLookForCustomTranslators() {
        // scenario
        doReturn(Optional.empty()).when(cdiUtils).getTypedBean(ConfigurationValueTranslator.class, Integer.class);

        // execution
        translator.translate("123", Integer.class);

        // validation
        verify(cdiUtils, times(1)).getTypedBean(ConfigurationValueTranslator.class, Integer.class);

    }

    @Test
    public void shouldUseDefaultTranslator() {
        // scenario
        doReturn(Optional.empty()).when(cdiUtils).getTypedBean(any(), any());
        final Integer expectedValue = new Random().nextInt();
        final String input = expectedValue.toString();

        // execution
        final Integer actualValue = translator.translate(input, Integer.class);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(expectedValue));
    }

}
