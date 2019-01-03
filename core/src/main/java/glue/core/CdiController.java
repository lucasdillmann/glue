package glue.core;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import java.util.Objects;

/**
 * Glue IoC controller class
 *
 * <p>This class initializes the Inversion of Control (IoC) mechanism and then starts the application lifecycle by
 * creating the needed instances from the IoC context. This class is, in some way, the heart of the Glue.</p>
 *
 * <p>The main goal here is to enable the entire Glue ecosystem. All modules are detected using this class, integrating
 * all the features provided by they.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
class CdiController {

    private final Class<?> applicationMainClass;
    private final WeldContainer injector;

    /**
     * Constructor with application main class initialization
     *
     * <p>This constructor initializes the IoC mechanism using the provided application main class to detect
     * the application base package.</p>
     *
     * @param applicationMainClass Application main class
     */
    CdiController(final Class<?> applicationMainClass) {
        Objects.requireNonNull(applicationMainClass);
        this.applicationMainClass = applicationMainClass;
        this.injector = new Weld()
                .enableDiscovery()
                .scanClasspathEntries()
                .containerId("Glue")
                .addPackages(true, applicationMainClass.getPackage())
                .skipShutdownHook()
                .initialize();
    }

    /**
     * Starts the application lifecycle
     *
     * <p>This modules uses the IoC context to start the application lifecycle. This is done by creating and firing-up
     * the {@link GlueApplicationContext} class, asking to start the application lifecycle.</p>
     */
    void start() {
        final GlueApplicationContext context = injector.select(GlueApplicationContext.class).get();
        context.setStartupClass(applicationMainClass);
        context.setIocProvider(injector);
        context.start();
    }
}
