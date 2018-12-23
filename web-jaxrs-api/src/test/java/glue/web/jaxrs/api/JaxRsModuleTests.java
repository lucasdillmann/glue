package glue.web.jaxrs.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test cases for {@link JaxRsModule}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@RunWith(MockitoJUnitRunner.class)
public class JaxRsModuleTests {

    @Mock
    private JaxRsInstaller installer;
    @Mock
    private Logger logger;

    private JaxRsModule module;

    @Before
    public void setup() {
        this.module = new JaxRsModule(installer, logger);
    }

    @Test
    public void shouldCallInstallActionOnModuleStart() {
        // execution
        module.start();

        // validation
        verify(installer, times(1)).install();
    }

    @Test
    public void shouldCallUninstallActionOnModuleStop() {
        // execution
        module.stop();

        // validation
        verify(installer, times(1)).uninstall();
    }
}
