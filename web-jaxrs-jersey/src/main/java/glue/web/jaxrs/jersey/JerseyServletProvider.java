package glue.web.jaxrs.jersey;

import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * Jersey {@link Servlet} provider
 *
 * <p>This class is able to produce {@link Servlet} instances for Jersey using {@link JerseyServlet} qualifier.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@Singleton
public class JerseyServletProvider {

    private final JerseyServletConfig servletConfig;
    private final Logger logger;

    /**
     * Constructor with {@link JerseyServletConfig} and {@link Logger} initialization
     *
     * @param servletConfig Servlet config
     * @param logger Logger
     */
    @Inject
    public JerseyServletProvider(final JerseyServletConfig servletConfig, final Logger logger) {
        this.servletConfig = servletConfig;
        this.logger = logger;
    }

    /**
     * Produces a singleton instance of Jersey {@link Servlet}
     *
     * @return Jersey servlet instance
     * @throws ServletException When production fails
     */
    @Produces
    @JerseyServlet
    @Singleton
    public Servlet jerseyServlet() throws ServletException {
        logger.debug("Producing Servlet for Jersey using configuration from {}", servletConfig.getClass().getName());
        final ServletContainer container = new ServletContainer();
        container.init(servletConfig);

        return container;
    }

}
