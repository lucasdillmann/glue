package glue.web.jaxrs.jersey;

import glue.web.container.api.WebContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Test cases for {@link JerseyServletConfig}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@RunWith(MockitoJUnitRunner.class)
public class JerseyServletConfigTests {

    @Mock
    private JerseyConfiguration configuration;
    @Mock
    private WebContainer webContainer;
    @Mock
    private ServletContext servletContext;

    private JerseyServletConfig config;

    @Before
    public void setup() {
        doReturn(servletContext).when(webContainer).getServletContext();
        this.config = new JerseyServletConfig(configuration, webContainer);
    }

    @Test
    public void shouldReturnServletNameFromConfiguration() {
        // scenario
        final String expectedValue = Integer.toString(new Random().nextInt());
        doReturn(expectedValue).when(configuration).getServletName();

        // execution
        final String actualValue = config.getServletName();

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(expectedValue));
    }
}
