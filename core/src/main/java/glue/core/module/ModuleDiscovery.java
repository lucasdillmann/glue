package glue.core.module;

/**
 * Glue module discovery interface
 *
 * <p>This class enables module discovery using SPI. It's main goal is simple: know which modules are available in the
 * classpath and add them to the CDI context, enabling the discovery of beans implemented inside the module.</p>
 *
 * <p>This mechanism is an alternative to the {@code beans.xml} file. Both of them should work and developers are
 * free to choose which one fits best.</p>
 *
 * <p>As said before, any implementations of this interface is found using Java SPI. Remember to create the service
 * file when you create a module using it.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-24
 */
public interface ModuleDiscovery {

    /**
     * Returns the module main package for CDI bean discovery
     *
     * <p>This method returns the module main package. The value returned by this module will be used by CDI as the
     * base path to scan for beans.</p>
     *
     * <p>Remember that since the package returned by this method means to be the base package, if your module
     * return any other package than that only beans that reside inside the returned package will be discovered
     * recursively.</p>
     *
     * @return Module base package for CDI bean discovery
     */
    Package getPackage();
}
