package glue.core.logger;

import glue.core.module.ModuleDiscovery;

/**
 * Glue {@link ModuleDiscovery} implementation for logger module
 *
 * <p>This class enables the automatic Glue bean discovery for logger module.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
public class LoggerModuleDiscovery implements ModuleDiscovery {

    /**
     * Returns the base package for the module
     *
     * @return Base package for logger module
     */
    @Override
    public Package getPackage() {
        return getClass().getPackage();
    }
}
