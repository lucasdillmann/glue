package glue.core;

import glue.core.module.ModuleDiscovery;
import org.jboss.weld.bootstrap.spi.BeanDiscoveryMode;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
class ApplicationController {

    private final WeldContainer injector;

    /**
     * Constructor with application main class initialization
     *
     * <p>This constructor initializes the IoC mechanism using the provided application main class to detect
     * the application base package.</p>
     *
     * @param applicationMainClass Application main class
     */
    ApplicationController(Class<?> applicationMainClass) {
        final Weld weld = new Weld()
                .enableDiscovery()
                .setBeanDiscoveryMode(BeanDiscoveryMode.ALL)
                .addPackages(true, applicationMainClass.getPackage())
                .addPackages(true, getClass().getPackage());

        getModulesPackages().forEach(modulePackage -> weld.addPackages(true, modulePackage));
        this.injector = weld.scanClasspathEntries().initialize();
    }

    /**
     * Detects and returns all modules {@link Package}s to be included in the IoC context
     *
     * @return All modules packages
     */
    private List<Package> getModulesPackages() {
        final Iterator<ModuleDiscovery> modules = ServiceLoader
                .load(ModuleDiscovery.class)
                .iterator();

        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(modules, 0), false)
                .map(ModuleDiscovery::getPackage)
                .collect(Collectors.toList());
    }

    /**
     * Starts the application lifecycle
     *
     * <p>This modules uses the IoC context to start the application lifecycle. This is done by creating and firing-up
     * the {@link LifecycleController} class.</p>
     */
    void start() {
        injector.select(LifecycleController.class)
                .get()
                .start();
    }
}
