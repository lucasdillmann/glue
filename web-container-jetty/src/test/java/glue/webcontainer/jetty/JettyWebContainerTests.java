package glue.webcontainer.jetty;

import org.eclipse.jetty.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test cases for {@link JettyWebContainer}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-03
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Server.class)
public class JettyWebContainerTests {

    private Server server;
    private Logger logger;

    private JettyWebContainer webContainer;

    @Before
    public void setup() {
        this.server = PowerMockito.mock(Server.class);
        this.logger = PowerMockito.mock(Logger.class);
        this.webContainer = new JettyWebContainer(server, logger);
    }

    @Test
    public void shouldStartJetty() throws Exception {
        // scenario
        PowerMockito.doNothing().when(server).start();

        // execution
        webContainer.start();

        // validation
        verify(server, times(1)).start();
    }

    @Test
    public void shouldStopJetty() throws Exception {
        // scenario
        PowerMockito.doNothing().when(server).stop();

        // execution
        webContainer.stop();

        // validation
        verify(server, times(1)).stop();
    }

}
