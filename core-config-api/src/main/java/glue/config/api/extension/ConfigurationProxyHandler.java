package glue.config.api.extension;

import glue.config.api.exception.ConfigurationException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Java Proxy {@link InvocationHandler} implementation for Configuration API
 *
 * <p>This class implements the Java Proxy {@link InvocationHandler} handler. It implementation is responsible to
 * handle all calls to configuration proxies, forwarding the configuration resolution and translation when required.</p>
 *
 * <p>In another words, this class is the bridge between the created instances and the internal configuration API
 * procedures.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@Singleton
class ConfigurationProxyHandler implements InvocationHandler {

    private final ConfigurationResolverBridge resolver;
    private final ConfigurationValueTranslatorBridge valueTranslator;
    private final ConfigurationContainerFacade containerFacade;
    private final Logger logger;

    /**
     * Package protected constructor with {@link ConfigurationResolverBridge}, {@link ConfigurationValueTranslatorBridge}
     * and {@link Logger} initialization
     *
     * @param resolver Configuration resolver
     * @param valueTranslator Configuration values translator
     * @param containerFacade Configuration container translator facade
     * @param logger Logger
     */
    @Inject
    ConfigurationProxyHandler(final ConfigurationResolverBridge resolver,
                              final ConfigurationValueTranslatorBridge valueTranslator,
                              final ConfigurationContainerFacade containerFacade,
                              final Logger logger) {
        this.resolver = resolver;
        this.valueTranslator = valueTranslator;
        this.containerFacade = containerFacade;
        this.logger = logger;
    }

    /**
     * Method fired by Java Proxy API when any method on a configuration proxy is called
     *
     * <p>This method intercepts all calls to configuration proxies. When called, it will start the configuration
     * resolution process.</p>
     *
     * @param proxyInstance Proxy instance when the call happened
     * @param calledMethod Called method
     * @param arguments Call arguments
     * @return Resolved configuration value
     * @throws Throwable When any exceptions happens
     */
    @Override
    public Object invoke(final Object proxyInstance,
                         final Method calledMethod,
                         final Object[] arguments) throws Throwable {

        logger.debug(
                "Method {} from interface {} called. Starting configuration resolution.",
                calledMethod.getName(),
                calledMethod.getDeclaringClass().getName()
        );

        if (calledMethod.isDefault()) {
            logger.debug(
                    "Method {} from interface {} has default modifier. Call will be forwarded to the implementation.",
                    calledMethod.getName(),
                    calledMethod.getDeclaringClass().getName()
            );
            return invokeDefaultMethod(proxyInstance, calledMethod, arguments);
        } else {
            if (arguments != null && arguments.length > 0)
                logger.warn("Method {} from configuration interface {} was called using arguments. They will be ignored " +
                        "since the feature isn't supported.");

            return invokeConfigurationResolution(calledMethod);
        }

    }

    /**
     * Configuration resolution method
     *
     * <p>This metohds executes the configuration resolution on the provided method. That includes the metadata extraction,
     * value resolution, translation and more.</p>
     *
     * @param calledMethod Called method on proxy to start the configuration resolution
     * @return Resolved and translated value
     * @throws Throwable When any exception happens
     */
    private Object invokeConfigurationResolution(final Method calledMethod) throws Throwable {
        final ConfigurationMetadata metadata = new ConfigurationMetadata(calledMethod);

        final Class<?> returnType = metadata.getTargetType();
        if (containerFacade.isAContainer(returnType)) {
            final Class<?> targetType = containerFacade.getTargetConfigurationValueType(
                    returnType, metadata.getGenericReturnType()
            );
            final Object configurationValue = getConfigurationValueFromResolver(metadata, targetType);
            return containerFacade.translate(configurationValue, returnType);
        } else {
            return getConfigurationValueFromResolver(metadata, returnType);
        }

    }

    /**
     * Executes the configuration resolution
     *
     * @param metadata Configuration metadata
     * @param targetType Target type to convert the configurtion value to
     * @param <T> Generic target type
     * @return Resolved configuration value
     */
    private <T> T getConfigurationValueFromResolver(final ConfigurationMetadata metadata,
                                                    final Class<T> targetType) {
        logger.debug(
                "Resolving configuration key {} of type {} with default value of '{}'",
                metadata.getKey(),
                targetType.getName(),
                metadata.getDefaultValue()
        );

        final String configurationValue = resolver.resolve(metadata.getKey(), metadata.getDefaultValue());
        if (configurationValue == null && metadata.isRequired())
            throw new ConfigurationException("Configuration value for key '" + metadata.getKey() + "' is required but no value was found");

        return valueTranslator.translate(configurationValue, targetType);
    }

    /**
     * Fires the default implementation of the method in the interface
     *
     * <p>When a method is detected in interface with default implementation the configuration API will not intercept
     * the execution of it. Instead the actual implementation will be called. This method does the invocation of it
     * using the {@link MethodHandles} API.</p>
     *
     * @param proxyInstance Proxy instance where the call happened
     * @param calledMethod Called method in the proxy
     * @param arguments Call arguments
     * @return Returned value by the default implementation
     * @throws Throwable When any exception happens
     */
    private Object invokeDefaultMethod(final Object proxyInstance,
                                       final Method calledMethod,
                                       final Object[] arguments) throws Throwable {
        final Class<?> configurationInterface = calledMethod.getDeclaringClass();
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class);
        constructor.setAccessible(true);

        return constructor
                .newInstance(configurationInterface)
                .in(configurationInterface)
                .unreflectSpecial(calledMethod, configurationInterface)
                .bindTo(proxyInstance)
                .invokeWithArguments(arguments);
    }
}
