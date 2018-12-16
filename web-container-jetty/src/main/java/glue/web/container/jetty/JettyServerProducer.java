package glue.web.container.jetty;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Jetty {@link Server} producer for CDI
 *
 * <p>This class produces Jetty {@link Server} instances using configuration values retrieved from {@link JettyConfiguration}.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-03
 */
public class JettyServerProducer {

    private final JettyConfiguration configuration;
    private final Logger logger;

    /**
     * Constructor with {@link JettyConfiguration} and {@link Logger} initialization
     *
     * @param configuration Jetty configuration
     * @param logger        Logger instance
     */
    @Inject
    public JettyServerProducer(JettyConfiguration configuration, Logger logger) {
        this.configuration = configuration;
        this.logger = logger;
    }

    /**
     * Produces a Jetty {@link Server} instance configured using definitions from {@link JettyConfiguration}
     *
     * @return Jetty Server instance
     */
    @Produces
    @Singleton
    public Server server() {
        logger.debug("Producing a Jetty Server instance");
        logger.debug("Server listen port: {}", configuration.getPort());

        return new Server(configuration.getPort());
    }
}
