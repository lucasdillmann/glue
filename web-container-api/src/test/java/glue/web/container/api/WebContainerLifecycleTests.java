package glue.web.container.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test cases for {@link WebContainerLifecycle}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-26
 */
@RunWith(MockitoJUnitRunner.class)
public class WebContainerLifecycleTests {

    @Mock
    private WebContainer webContainer;
    @Mock
    private Logger logger;

    private WebContainerLifecycle lifecycle;

    @Before
    public void setup() {
        lifecycle = new WebContainerLifecycle(webContainer, logger);
    }

    @Test
    public void shouldStartContainer() {
        // execution
        lifecycle.start();

        // validation
        verify(webContainer, times(1)).startContainer();
    }

    @Test
    public void shouldStopContainer() {
        // execution
        lifecycle.stop();

        // validation
        verify(webContainer, times(1)).stopContainer();
    }

}
