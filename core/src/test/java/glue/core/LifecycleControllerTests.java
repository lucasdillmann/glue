package glue.core;

import glue.core.exception.StartupException;
import glue.core.module.ModuleLifecycle;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link LifecycleController}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-26
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GlueApplicationContext.class)
public class LifecycleControllerTests {

    @Mock
    private Instance<ModuleLifecycle> modules;
    @Mock
    private Instance<Logger> logger;
    @Mock
    private Instance<ShutdownListener> shutdownListener;
    @Mock
    private Iterator<ModuleLifecycle> modulesIterator;
    @Mock
    private Spliterator<ModuleLifecycle> modulesSpliterator;
    @Mock
    private ShutdownListener shutdownListenerDelegate;
    @Mock
    private Logger loggerDelegate;
    @Mock
    private GlueApplicationContext applicationContext;
    @Mock
    private Instance<GlueApplicationContext> applicationContextInstance;
    @Mock
    private WeldContainer weldContainer;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        GlueApplication.initStartupTime();
        doReturn(modulesIterator).when(modules).iterator();
        doReturn(applicationContext).when(applicationContextInstance).get();
        doReturn(modulesSpliterator).when(modules).spliterator();
        doReturn(shutdownListenerDelegate).when(shutdownListener).get();
        doReturn(loggerDelegate).when(logger).get();
        doReturn(weldContainer).when(applicationContext).getIocProvider();
        doReturn(true).when(modulesIterator).hasNext();
    }

    @Test
    public void shouldStartJvmShutdownListener() {
        // scenario
        final LifecycleController controller = new LifecycleController(modules, logger, shutdownListener, applicationContextInstance);

        // execution
        controller.start();

        // validation
        verify(shutdownListenerDelegate, times(1)).start(Mockito.any(Runnable.class));
    }

    @Test
    public void shouldStartModules() {
        // scenario
        final ModuleLifecycle testModule = mock(ModuleLifecycle.class);
        doCallRealMethod().when(testModule).getStartPriority();
        doCallRealMethod().when(testModule).getStopPriority();
        final List<ModuleLifecycle> testModules = Arrays.asList(testModule);
        doReturn(testModules.spliterator()).when(modules).spliterator();
        final LifecycleController controller = new LifecycleController(modules, logger, shutdownListener, applicationContextInstance);

        // execution
        controller.start();

        // validation
        verify(testModule, times(1)).start();
    }

    @Test
    public void shouldStopModules() {
        // scenario
        final ModuleLifecycle testModule = mock(ModuleLifecycle.class);
        doCallRealMethod().when(testModule).getStartPriority();
        doCallRealMethod().when(testModule).getStopPriority();
        final List<ModuleLifecycle> testModules = Arrays.asList(testModule);
        doReturn(testModules.spliterator()).when(modules).spliterator();
        final LifecycleController controller = spy(new LifecycleController(modules, logger, shutdownListener, applicationContextInstance));
        doNothing().when(controller).shutdownJvm();

        // execution
        controller.start();
        controller.stop();

        // validation
        verify(testModule, times(1)).stop();
    }

    @Test
    public void shouldStopApplicationWhenNoModuleIsFound() {
        // scenario
        doReturn(true).when(modules).isUnsatisfied();

        // validation
        expectedException.expect(any(StartupException.class));

        // execution
        new LifecycleController(modules, logger, shutdownListener, applicationContextInstance);
    }

    @Test
    public void shouldStopApplicationWhenNoLoggerIsFound() {
        // scenario
        doReturn(true).when(logger).isUnsatisfied();

        // validation
        expectedException.expect(any(StartupException.class));

        // execution
        new LifecycleController(modules, logger, shutdownListener, applicationContextInstance);

    }

}
