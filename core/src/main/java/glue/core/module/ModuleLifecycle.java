package glue.core.module;

/**
 * Glue module lifecycle interface
 *
 * <p>This interfaces allows to any module integrate himself with the Glue main lifecycle. When the application is
 * starting up or beign stopped implementations of this interface will be called, allowing the module to do what they
 * need to do in such scenarios.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-24
 */
public interface ModuleLifecycle {

    /**
     * Starts the module
     *
     * <p>This method starts the module lifecycle, firing up all the required actions to allow the features provided
     * by it to start working.</p>
     */
    void start();

    /**
     * Stops the module
     *
     * <p>This method stop the module lifecycle, shutting down the features provided by it.</p>
     */
    void stop();

    /**
     * Defines the module startup priority
     *
     * <p>This method returns the priority that should be taken into account when the application is starting up. The
     * default value is {@link Priority#REGULAR}.</p>
     *
     * @return Module startup priority
     */
    default Priority getStartPriority() {
        return Priority.REGULAR;
    }

    /**
     * Defines the module shutdown priority
     *
     * <p>This method defines the priority to take into account when the application is shutting down. The default
     * value is {@link Priority#REGULAR}.</p>
     *
     * @return Module stop priority
     */
    default Priority getStopPriority() {
        return Priority.REGULAR;
    }

}
