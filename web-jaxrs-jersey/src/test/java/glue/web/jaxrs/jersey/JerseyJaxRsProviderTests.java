package glue.web.jaxrs.jersey;

import glue.core.GlueApplicationContext;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import javax.servlet.Servlet;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link JerseyJaxRsProvider}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GlueApplicationContext.class)
public class JerseyJaxRsProviderTests {

    @Mock
    private ServletContainer servlet;
    @Mock
    private JerseyConfiguration configuration;
    @Mock
    private GlueApplicationContext applicationContext;
    @Mock
    private Logger logger;

    private JerseyApplication application;
    private JerseyJaxRsProvider provider;

    @Before
    public void setup() {
        doReturn(getClass()).when(applicationContext).getStartupClass();
        this.application = new JerseyApplication();
        this.provider = new JerseyJaxRsProvider(servlet, application, configuration, applicationContext, logger);
    }

    @Test
    public void shouldStopServlet() {
        // execution
        provider.stop();

        // validation
        verify(servlet, times(1)).destroy();
    }

    @Test
    public void shouldApplyApplicationPackageConfiguration() {
        // scenario
        final String expectedValue = getClass().getPackage().getName();

        // execution
        final Map<String, String> parameters = provider.getServletInitParameters();

        // validation
        assertNotNull(parameters);
        assertNotNull(parameters.get(JerseyJaxRsProvider.JERSEY_PACKAGE_SCAN_PROPERTY));
        assertThat(parameters.get(JerseyJaxRsProvider.JERSEY_PACKAGE_SCAN_PROPERTY), is(expectedValue));
    }

    @Test
    public void shouldReturnContextPathFromConfiguration() {
        // scenario
        final String expectedValue = Integer.toString(new Random().nextInt()) + "/*";
        doReturn(expectedValue).when(configuration).getContextPath();

        // execution
        final String actualValue = provider.getContextPath();

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(expectedValue));
    }

    @Test
    public void shouldAddAsteriskAtContextPathWhenMissing() {
        // scenario
        final String randomValue = Integer.toString(new Random().nextInt());
        final String expectedValue = randomValue + "/*";
        doReturn(randomValue).when(configuration).getContextPath();

        // execution
        final String actualValue = provider.getContextPath();

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(expectedValue));
    }

    @Test
    public void shoudReturnServletInstance() {
        // execution
        final Servlet servlet = provider.getServlet();

        // validation
        assertNotNull(servlet);
    }
}
