package glue.config.api.extension;

import glue.config.api.extension.artifacts.ConfigurationProxyHandlerArtifact;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link ConfigurationProxyHandler}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationProxyHandlerTests {

    @Mock
    private ConfigurationResolverBridge resolver;
    @Mock
    private ConfigurationValueTranslatorBridge translator;
    @Mock
    private ConfigurationContainerFacade containerFacade;
    @Mock
    private Logger logger;
    @Mock
    private ConfigurationProxyHandlerArtifact artifact;

    private ConfigurationProxyHandler handler;

    @Before
    public void setup() {
        this.handler = new ConfigurationProxyHandler(resolver, translator, containerFacade, logger);
    }

    @Test
    public void shouldCallDefaultMethods() throws Throwable {
        // scenario
        final Method method = ConfigurationProxyHandlerArtifact.class.getMethod("getDefaultValue");
        final Integer expectedValue = 123;

        // execution
        final Object actualValue = handler.invoke(artifact, method, null);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(expectedValue));
        verify(resolver, times(0)).resolve(anyString(), anyString());
        verify(translator, times(0)).translate(anyString(), Mockito.any());
    }

    @Test
    public void shouldInvokeResolutionAndTranslation() throws Throwable {
        // scenario
        final Method method = ConfigurationProxyHandlerArtifact.class.getMethod("getTest");
        final Integer expectedValue = 123;
        final String expectedKey = "testArtifact.test";
        doReturn(expectedValue.toString()).when(resolver).resolve(eq(expectedKey), anyString());
        doReturn(expectedValue).when(translator).translate(expectedValue.toString(), Integer.class);

        // execution
        final Object actualValue = handler.invoke(artifact, method, null);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(expectedValue));
        verify(resolver, times(1)).resolve(eq(expectedKey), anyString());
        verify(translator, times(1)).translate(expectedValue.toString(), Integer.class);
    }

    @Test
    public void shouldDetectAndHandleOptionalReturnTypes() throws Throwable {
        // scenario
        final Method method = ConfigurationProxyHandlerArtifact.class.getMethod("getTestWithOptional");
        final Integer expectedValue = 123;
        final String expectedKey = "testArtifact.test";
        doReturn(expectedValue.toString()).when(resolver).resolve(eq(expectedKey), anyString());
        doReturn(expectedValue).when(translator).translate(expectedValue.toString(), Integer.class);

        // execution
        final Object actualValue = handler.invoke(artifact, method, null);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(instanceOf(Optional.class)));

        final Optional<Integer> optionalInstance = (Optional<Integer>) actualValue;
        assertThat(optionalInstance.isPresent(), is(true));
        assertThat(optionalInstance.get(), is(expectedValue));
    }

}
