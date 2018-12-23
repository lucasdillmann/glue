package glue.web.jaxrs.jersey;

import glue.core.GlueApplicationContext;
import glue.web.jaxrs.api.JaxRsProvider;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Servlet;
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

    private final Servlet servlet;
    private final JerseyConfiguration configuration;
    private final GlueApplicationContext applicationContext;
    private final Logger logger;

    /**
     * Constructor with {@link Servlet}, {@link JerseyConfiguration}, {@link GlueApplicationContext} and
     * {@link Logger} initialization
     *
     * @param servlet Jersey servlet
     * @param configuration Jersey configuration
     * @param applicationContext Glue application context
     * @param logger Logger
     */
    @Inject
    public JerseyJaxRsProvider(final @JerseyServlet Servlet servlet,
                               final JerseyConfiguration configuration,
                               final GlueApplicationContext applicationContext,
                               final Logger logger) {
        this.servlet = servlet;
        this.logger = logger;
        this.applicationContext = applicationContext;
        this.configuration = configuration;
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
        // NO-OP
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
}
