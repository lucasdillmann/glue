package glue.web.jaxrs.jersey;

import glue.core.GlueApplicationContext;
import glue.web.jaxrs.api.JaxRsProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Servlet;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;

/**
 * JAX-RS provider implementation for Jersey
 *
 * <p>This class implements the {@link JaxRsProvider} Glue API using Jersey as the back provider.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@Singleton
public class JerseyJaxRsProvider implements JaxRsProvider {

    static final String JERSEY_PACKAGE_SCAN_PROPERTY = "jersey.config.server.provider.packages";

    private final ServletContainer servlet;
    private final JerseyConfiguration configuration;
    private final GlueApplicationContext applicationContext;
    private final Logger logger;
    private final ResourceConfig resourceConfig;

    /**
     * Constructor with {@link Servlet}, {@link JerseyConfiguration}, {@link GlueApplicationContext} and
     * {@link Logger} initialization
     *
     * @param servlet            Jersey servlet
     * @param configuration      Jersey configuration
     * @param applicationContext Glue application context
     * @param logger             Logger
     */
    @Inject
    public JerseyJaxRsProvider(final @JerseyServlet ServletContainer servlet,
                               final Application application,
                               final JerseyConfiguration configuration,
                               final GlueApplicationContext applicationContext,
                               final Logger logger) {
        this.servlet = servlet;
        this.logger = logger;
        this.applicationContext = applicationContext;
        this.configuration = configuration;
        this.resourceConfig = ResourceConfig.forApplication(application);
    }

    /**
     * Starts the provider and return the {@link Servlet} to be used
     *
     * <p>This method notifies the provider that application is starting and expectes a Servlet instance as return
     * to integrate the provider under current web container.</p>
     *
     * @return Servlet instance from provider implementation
     */
    @Override
    public void start() {
        logger.info("Reloading Jersey with updated configurations");
        this.servlet.reload(resourceConfig);
    }

    /**
     * Notifies provider about application shutdown
     *
     * <p>This method is called when application is stopping, allowing provider to gracefully stops.</p>
     */
    @Override
    public void stop() {
        logger.info("Stopping Jersey");
        servlet.destroy();
    }

    /**
     * Returns the provider servlet context path
     *
     * @return Servlet context path
     */
    @Override
    public String getContextPath() {
        String contextPath = configuration.getContextPath();
        if (!contextPath.endsWith("/*")) {
            contextPath = contextPath + "/*";
            logger.warn("Jersey JAX-RS context path didn't ends with '/*', fixing to '{}'", contextPath);
        }

        return contextPath;
    }

    /**
     * Returns servlet instance for the provider
     *
     * @return Servlet instance
     */
    @Override
    public Servlet getServlet() {
        return servlet;
    }

    /**
     * Returns all servlet init parameters
     *
     * @return Map of init parameters
     */
    @Override
    public Map<String, String> getServletInitParameters() {
        final Map<String, String> parameters = new HashMap<>();
        final String applicationPackage = applicationContext
                .getStartupClass()
                .getPackage()
                .getName();

        parameters.put(JERSEY_PACKAGE_SCAN_PROPERTY, applicationPackage);
        return parameters;
    }

    /**
     * Register a component class under JAX-RS context
     *
     * @param componentClass Component class
     * @param contracts      Contract classes
     */
    @Override
    public void registerClass(final Class<?> componentClass, final Class<?>... contracts) {
        if (contracts == null || contracts.length == 0)
            resourceConfig.register(componentClass);
        else
            resourceConfig.register(componentClass, contracts);

        logger.info("Class registered: {}", componentClass.getName());
    }

    /**
     * Registers a singleton instance of a object and bound it to contract classes
     *
     * @param singletonInstance Singleton instance to be registered
     * @param contracts         Contract classes
     */
    @Override
    public void registerSingleton(final Object singletonInstance, final Class<?>... contracts) {
        if (contracts == null || contracts.length == 0)
            resourceConfig.register(singletonInstance);
        else
            resourceConfig.register(singletonInstance, contracts);

        logger.info("Singleton registered: {}", singletonInstance);
    }

}
