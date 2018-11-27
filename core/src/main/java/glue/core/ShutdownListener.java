package glue.core;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

/**
 * JVM shutdown listener
 *
 * <p>This class registers himself under the JVM {@link Runtime} to know when the JVM receives the SIGTERM signal
 * from the OS, forwarding the message to the consumer when it arrives.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
@Singleton
class ShutdownListener {

    private final Logger logger;
    private Thread currentListener;

    /**
     * Constructor with logger initialization
     *
     * @param logger Logger to be used
     */
    @Inject
    public ShutdownListener(Logger logger) {
        this.logger = logger;
    }

    /**
     * Starts the listening
     *
     * <p>This method starts the listening for JVM shutdown events, using the provided {@link Runnable} command
     * to notify when such event arrives.</p>
     *
     * @param consumer Event consumer to be called when the JVM shutdown event arrives
     */
    void start(Runnable consumer) {
        Objects.requireNonNull(consumer);

        if (!Objects.isNull(currentListener))
            throw new IllegalStateException("Listening can't be started. It was already started previously.");

        final Runnable targetRunnable = () -> {
            logger.info("Application shutdown event received");
            consumer.run();
        };

        logger.info("Begin listening for JVM shutdown events");
        currentListener = new Thread(targetRunnable);
        Runtime.getRuntime().addShutdownHook(currentListener);
    }

    /**
     * Stops the listening
     *
     * <p>This method stops the listening for JVM shutdown events by removing the previously registered listener.</p>
     */
    void stop() {
        if (Objects.isNull(currentListener))
            throw new IllegalStateException("The listeneing can't be stopped since it isn't running");

        logger.info("Stopping te listening for JVM shutdown events");
        Runtime.getRuntime().removeShutdownHook(currentListener);
        currentListener = null;
    }
}
