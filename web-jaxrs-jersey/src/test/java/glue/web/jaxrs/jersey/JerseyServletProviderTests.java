package glue.web.jaxrs.jersey;

import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import javax.servlet.Servlet;

import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Test cases for {@link JerseyServletProvider}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServletContainer.class, JerseyServletProvider.class})
public class JerseyServletProviderTests {

    @Mock
    private Logger logger;
    @Mock
    private JerseyServletConfig servletConfig;
    @Mock
    private ServletContainer servletContainer;

    private JerseyServletProvider provider;

    @Before
    public void setup() throws Exception {
        whenNew(ServletContainer.class).withNoArguments().thenReturn(servletContainer);
        this.provider = new JerseyServletProvider(servletConfig, logger);
    }

    @Test
    public void shouldProduceJerseyServlet() throws Exception {
        // execution
        final Servlet servlet = provider.jerseyServlet();

        // validation
        assertNotNull(servlet);
    }

}
