package glue.core.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.Optional;

/**
 * SLF4J {@link Logger} provider
 *
 * <p>This class produces {@link Logger} instances for SLF4J backed by the Logback implementation. Instances
 * are produced using the class where the injection is happening as the target class for the logger.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
@Default
public class LoggerProvider {

    /**
     * Produces a SLF4J {@link Logger} instance
     *
     * <p>This method produces {@link Logger} instances for SLF4J using Logback as the delegate implementation.
     * {@link InjectionPoint} from CDI is used to discover the class where the injection is happening, using it as
     * the target class for the logger.</p>
     *
     * @param injectionPoint Injection details
     * @return Produced logger
     */
    @Produces
    public Logger logger(final InjectionPoint injectionPoint) {
        final Class<?> client = injectionPoint.getMember().getDeclaringClass();
        final String loggerName = Optional
                .ofNullable(client)
                .map(Class::getName)
                .orElse(Logger.ROOT_LOGGER_NAME);
        return LoggerFactory.getLogger(loggerName);
    }

}
