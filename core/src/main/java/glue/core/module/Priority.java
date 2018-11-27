package glue.core.module;

/**
 * Module startup and shutdown priority enumeration
 *
 * <p>This enumeration defines all available options for priority for modules shutdown and startup events.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-24
 */
public enum Priority {

    /**
     * Maximum priority. Modules using this option should be started/stopped first.
     */
    HIGH(2),

    /**
     * Default, regular priority. Modules using this option will be started or stopped regularly.
     */
    REGULAR(1),

    /**
     * Minimum priority. Modules using this option should be started/stopped last.
     */
    LOW(0);

    private final int intValue;

    /**
     * Constructor with comparison integer value
     *
     * @param intValue Integer value for the option
     */
    Priority(int intValue) {
        this.intValue = intValue;
    }

    /**
     * Return the integer comparison value
     *
     * @return Integer comparison value
     */
    public int asInteger() {
        return intValue;
    }
}
