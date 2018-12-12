package glue.config.api.extension;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
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
    private Instance instance;
    @Mock
    private Logger logger;

    private ConfigurationValueTranslatorBridge translator;

    @Before
    public void setup() {
        doReturn(instance).when(instance).select((TypeLiteral) anyObject());
        doReturn(false).when(instance).isAmbiguous();
        this.translator = new ConfigurationValueTranslatorBridge(instance, logger);
    }


    @Test
    public void shouldLookForCustomTranslators() {
        // scenario
        doReturn(true).when(instance).isUnsatisfied();

        // execution
        translator.translate("123", Integer.class);

        // validation
        verify(instance, times(1)).select(any(TypeLiteral.class));

    }

    @Test
    public void shouldUseDefaultTranslator() {
        // scenario
        doReturn(true).when(instance).isUnsatisfied();
        final Integer expectedValue = new Random().nextInt();
        final String input = expectedValue.toString();

        // execution
        final Integer actualValue = translator.translate(input, Integer.class);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(expectedValue));
    }

}
