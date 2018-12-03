package glue.webcontainer.jetty;

import glue.core.util.ExceptionUtils;
import glue.webcontainer.api.WebContainer;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    /**
     * Constructor with server and logger initialization
     *
     * @param server Jetty server
     * @param logger Logger
     */
    @Inject
    public JettyWebContainer(Server server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    /**
     * Starts the container
     *
     * <p>This method starts the container, enabling the project to handle web requests.</p>
     */
    @Override
    public void start() {
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
    public void stop() {
        logger.info("Stopping Jetty");
        try {
            server.stop();
        } catch (final Exception ex) {
            new ExceptionUtils().rethrowAsUnchecked(ex);
        }
    }
}
