package glue.web.container.jetty;

import org.eclipse.jetty.server.*;
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
        logger.info("Creating Jetty server for {}:{}", configuration.getHost(), configuration.getPort());

        final Server server = new Server();
        final HttpConfiguration httpConfiguration = new HttpConfiguration();
        final HttpConnectionFactory connectionFactory = new HttpConnectionFactory(httpConfiguration);
        final ServerConnector connector = new ServerConnector(server, connectionFactory);

        httpConfiguration.setSendServerVersion(configuration.isShowJettyVersion());
        httpConfiguration.setSendXPoweredBy(configuration.isShowXPoweredBy());
        connector.setPort(configuration.getPort());
        connector.setHost(configuration.getHost());
        server.setConnectors(new Connector[] { connector });

        return server;
    }
}
