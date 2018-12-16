package glue.web.container.jetty;

import org.eclipse.jetty.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link JettyServerProducer}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-03
 */
@RunWith(MockitoJUnitRunner.class)
public class JettyServerProducerTests {

    @Mock
    private Logger logger;
    @Mock
    private JettyConfiguration configuration;

    private JettyServerProducer producer;

    @Before
    public void setup() {
        this.producer = new JettyServerProducer(configuration, logger);
    }

    @Test
    public void shouldProduceServerInstances() {
        // scenario
        doReturn(8090).when(configuration).getPort();

        // execution
        final Server server = producer.server();

        // validation
        assertNotNull(server);
        verify(configuration, atLeast(1)).getPort();
    }

}
