package glue.web.jaxrs.patch.api;

import glue.web.jaxrs.api.JaxRsProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test cases for {@link JaxRsPatchModuleLifecycle}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-25
 */
@RunWith(MockitoJUnitRunner.class)
public class JaxRsPatchModuleLifecycleTests {

    @Mock
    private Logger logger;
    @Mock
    private JaxRsProvider provider;

    private JaxRsPatchModuleLifecycle moduleLifecycle;

    @Before
    public void setup() {
        this.moduleLifecycle = new JaxRsPatchModuleLifecycle(provider, logger);
    }

    @Test
    public void shouldRegisterPatchInterceptorOnStartup() {
        // execution
        moduleLifecycle.start();

        // validation
        verify(provider, times(1)).registerClass(PatchReaderInterceptor.class);
    }
}
