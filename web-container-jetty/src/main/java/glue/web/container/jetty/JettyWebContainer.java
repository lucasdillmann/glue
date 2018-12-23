package glue.web.container.jetty;

import glue.core.util.ExceptionUtils;
import glue.web.container.api.WebContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Jetty implementation for {@link WebContainer}
 *
 * <p>This class implements the {@link WebContainer} API using Jetty as the provider.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-03
 */
@Singleton
public class JettyWebContainer implements WebContainer {

    private final Server server;
    private final Logger logger;
    private ServletContextHandler contextHandler;

    /**
     * Constructor with server and logger initialization
     *
     * @param server Jetty server
     * @param logger Logger
     */
    @Inject
    public JettyWebContainer(final Server server, final Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    /**
     * Internal initialization method
     *
     * <p>This method initializes internal properties and configurations like invoking basic operations using
     * injected {@link Server} to allow later registration of custom {@link Servlet}s.</p>
     */
    @PostConstruct
    private void init() {
        this.contextHandler = new ServletContextHandler();
        this.contextHandler.setContextPath("/");
        this.server.setHandler(contextHandler);
    }

    /**
     * Starts the container
     *
     * <p>This method starts the container, enabling the project to handle web requests.</p>
     */
    @Override
    public void startContainer() {
        logger.info("Starting Jetty");
        try {
            server.start();
        } catch (final Exception ex) {
            new ExceptionUtils().rethrowAsUnchecked(ex);
        }
    }

    /**
     * Stops the container
     *
     * <p>This method initiates the shutdown procedure of the web container.</p>
     */
    @Override
    public void stopContainer() {
        logger.info("Stopping Jetty");
        try {
            server.stop();
        } catch (final Exception ex) {
            logger.warn("Error stopping Jetty", ex);
        }
    }

    /**
     * Returns current servlet context
     *
     * @return Servlet context
     */
    @Override
    public ServletContext getServletContext() {
        return contextHandler.getServletContext();
    }

    /**
     * Starts a {@link Servlet} under the container
     *
     * <p>This method uses the provided {@link Servlet} instance to startContainer it under current web container.</p>
     *
     * @param servlet Servlet instance
     * @param contextPath Context path
     * @param initAttributes Init attributes
     */
    @Override
    public void startServlet(final Servlet servlet,
                             final String contextPath,
                             final Map<String, String> initAttributes) {
        Objects.requireNonNull(servlet);
        Objects.requireNonNull(contextPath);
        logger.debug("Starting servlet {} at context {}", servlet.getClass().getName(), contextPath);

        final ServletHolder holder = new ServletHolder(servlet);
        holder.setEnabled(true);
        holder.setDisplayName(servlet.getServletConfig().getServletName());
        Optional.ofNullable(initAttributes)
                .map(Map::entrySet)
                .map(Set::stream)
                .orElse(Stream.empty())
                .forEach(pair -> holder.setInitParameter(pair.getKey(), pair.getValue()));

        contextHandler.addServlet(holder, contextPath);
        logger.info("Servlet {} started at context path {}", servlet.getServletConfig().getServletName(), contextPath);
    }
}
