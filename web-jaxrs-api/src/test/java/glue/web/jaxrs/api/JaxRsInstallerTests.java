package glue.web.jaxrs.api;

import glue.web.container.api.WebContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.servlet.Servlet;
import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * Test cases for {@link JaxRsInstaller}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@RunWith(MockitoJUnitRunner.class)
public class JaxRsInstallerTests {

    @Mock
    private WebContainer webContainer;
    @Mock
    private JaxRsProvider provider;
    @Mock
    private Logger logger;
    @Mock
    private Servlet servlet;

    private JaxRsInstaller installer;

    @Before
    public void setup() {
        doReturn(servlet).when(provider).getServlet();
        doReturn("/*").when(provider).getContextPath();
        doReturn(new HashMap<>()).when(provider).getServletInitParameters();
        this.installer = new JaxRsInstaller(webContainer, provider,logger);
    }

    @Test
    public void shouldInstallProviderUnderContainer() {
        // execution
        installer.install();

        // validation
        verify(provider, times(1)).start();
        verify(webContainer, times(1)).startServlet(eq(servlet), anyString(), anyMap());
    }

    @Test
    public void shouldNotifyProviderAboutShutdown() {
        // execution
        installer.uninstall();

        // validation
        verify(provider, times(1)).stop();
    }
}
