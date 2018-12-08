package glue.config.api.extension;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.Optional;

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
    private final ConfigurationTranslatorBridge translator;
    private final Logger logger;

    /**
     * Package protected constructor with {@link ConfigurationResolverBridge}, {@link ConfigurationTranslatorBridge}
     * and {@link Logger} initialization
     *
     * @param resolver Configuration resolver
     * @param translator Configuration values translator
     * @param logger Logger
     */
    @Inject
    ConfigurationProxyHandler(final ConfigurationResolverBridge resolver,
                              final ConfigurationTranslatorBridge translator,
                              final Logger logger) {
        this.resolver = resolver;
        this.translator = translator;
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
        final String configurationKey = metadata.getKey();
        final String defaultValue = metadata.getDefaultValue();
        final Class<?> returnType = metadata.getTargetType();
        final Class<?> targetType = Optional.class.isAssignableFrom(returnType)
                ? extractTargetTypeFromOptional(calledMethod.getGenericReturnType())
                : returnType;

        logger.debug(
                "Resolving configuration key {} of type {} with default value of '{}'",
                configurationKey,
                returnType.getName(),
                defaultValue
        );

        final String configurationValue = resolver.resolve(configurationKey, defaultValue);
        final Object translatedValue = translator.translate(configurationValue, targetType);

        if (Optional.class.isAssignableFrom(returnType))
            return Optional.ofNullable(translatedValue);
        else
            return translatedValue;
    }

    /**
     * Extracts the generic type of an {@link Optional} return type. When no generic is found {@link Object} class is
     * returned.
     *
     * @param returnType Return type to be analyzed
     * @return Identified generic type
     */
    private Class<?> extractTargetTypeFromOptional(final Type returnType) {
        final ParameterizedType parameterizedType = (ParameterizedType) returnType;
        if (parameterizedType.getActualTypeArguments() == null || parameterizedType.getActualTypeArguments().length < 1)
            return Object.class;

        return (Class) parameterizedType.getActualTypeArguments()[0];
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
