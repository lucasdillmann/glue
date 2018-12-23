package glue.web.jaxrs.api;

import glue.web.container.api.WebContainer;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.Servlet;
import java.util.Map;

/**
 * JAX-RS installer facade
 *
 * <p>This class controls the installation and removal of the JAX-RS provider implementation under current web container.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
class JaxRsInstaller {

    private final WebContainer container;
    private final JaxRsProvider provider;
    private final Logger logger;

    /**
     * Constructor with {@link WebContainer}, {@link JaxRsProvider} and {@link Logger} initialization
     *
     * @param container Web container
     * @param provider JAX-RS provider
     * @param logger Logger
     */
    @Inject
    public JaxRsInstaller(final WebContainer container,
                          final JaxRsProvider provider,
                          final Logger logger) {
        this.container = container;
        this.provider = provider;
        this.logger = logger;
    }

    /**
     * Installs current JAX-RS provider under the web container to start receiving requests
     */
    void install() {
        logger.info("Starting JAX-RS provider {}", provider.getClass().getSimpleName());
        provider.start();

        final Servlet servlet = provider.getServlet();
        final String contextPath = provider.getContextPath();
        final Map<String, String> initAttributes = provider.getServletInitParameters();

        logger.info("Starting JAX-RS under current web container");
        container.startServlet(servlet, contextPath, initAttributes);
    }

    /**
     * Stops current JAX-RS provider, notifying about the application shutdown
     */
    void uninstall() {
        logger.info("Stopping JAX-RS provider {}", provider.getClass().getSimpleName());
        provider.stop();
    }
}
