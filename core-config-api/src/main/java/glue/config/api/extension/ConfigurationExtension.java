package glue.config.api.extension;

import glue.config.api.annotation.ConfigurationInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * CDI extension for configuration API
 *
 * <p>This extension enables the injection of {@link glue.config.api.annotation.ConfigurationInterface} annotated
 * interfaces using CDI.</p>
 *
 * <p>This extension analyzes all injection points and looks for the ones that uses configuration interfaces.
 * When one is detected a dynamic producer is created for the configuration interface, allowing the injection
 * without a concrete implementation for the interface.</p>
 *
 * <p>The creation of the interfaces instances is done using dynamic proxies. For more information read the
 * {@link ConfigurationProxyFactory} documentation.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
public final class ConfigurationExtension implements Extension {

    private final Set<Class<?>> configurationInterfaces;
    private final Logger logger;

    /**
     * Default constructor
     */
    public ConfigurationExtension() {
        this.configurationInterfaces = new HashSet<>();
        this.logger = LoggerFactory.getLogger(getClass());
    }

    /**
     * Detects CDI injection points using configuration interfaces
     *
     * <p>This method observes {@link ProcessInjectionPoint} events and looks for configuration interfaces injections.
     * When one is detected the injection point will be stored to be created a dynamic producer for it later on the
     * CDI startup cycle.</p>
     *
     * @param injectionPoint CDI injection point
     */
    private void detectConfigurationInterfaces(final @Observes ProcessInjectionPoint injectionPoint) {
        final Type targetType = injectionPoint.getInjectionPoint().getType();
        if (!(targetType instanceof Class))
            return;

        final Class<?> targetClass = (Class<?>) targetType;
        final boolean isConfiguration = targetClass.getAnnotation(ConfigurationInterface.class) != null;
        if (!isConfiguration)
            return;

        configurationInterfaces.add(targetClass);

    }

    /**
     * Observes the {@link AfterBeanDiscovery} event and registers dynamic producers for the configuration
     * interfaces in use
     *
     * <p>This method creates a dynamic producer for every configuraton interface detected in injection points by the
     * method {@link #detectConfigurationInterfaces(ProcessInjectionPoint)}. All dynamic CDI producers will
     * simple forward the bean creation to {@link ConfigurationProxyFactory} to handle.</p>
     *
     * @param afterBeanDiscovery After bean discovery event
     * @param beanManager Bean manager instance
     */
    private void createDynamicConfigurationProducers(final @Observes AfterBeanDiscovery afterBeanDiscovery,
                                                     final BeanManager beanManager) {

        logger.debug("{} configuration interfaces detected in injection points", configurationInterfaces.size());

        configurationInterfaces
                .forEach(configurationInterface -> {
                    addDynamicProducer(afterBeanDiscovery, beanManager, configurationInterface);
                });
    }

    /**
     * Adds a dynamic producer for the given configuration interface
     *
     * @param afterBeanDiscovery After bean discovery event to register the producer on
     * @param beanManager Bean manager instance
     * @param configurationInterface Configuration interface to create the producer for
     */
    private void addDynamicProducer(final AfterBeanDiscovery afterBeanDiscovery,
                                    final BeanManager beanManager,
                                    final Class<?> configurationInterface) {
        logger.debug("Creating dynamic configuration producer for {}", configurationInterface.getName());

        afterBeanDiscovery
                .addBean()
                .types(configurationInterface)
                .qualifiers(new Default.Literal(), new Any.Literal())
                .createWith(instance -> produceConfigurationProxy(configurationInterface, beanManager))
                .produceWith(instance -> produceConfigurationProxy(configurationInterface, beanManager));
    }

    /**
     * Configuration instances producer methods
     *
     * <p>This method is called when a dynamic configuration instance is required. It will use the provided bean
     * manager to get the factory instance and then forward the production to it.</p>
     *
     * @param configurationInterface Configuration interface to create the instance for
     * @param beanManager Bean manager
     * @param <T> Generic configuration interface type
     * @return Created configuration instance
     */
    private <T> T produceConfigurationProxy(final Class<T> configurationInterface,
                                            final BeanManager beanManager) {

        return beanManager
                .createInstance()
                .select(ConfigurationProxyFactory.class)
                .get()
                .build(configurationInterface);
    }

}
