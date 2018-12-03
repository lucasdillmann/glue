package glue.config.api.translator;

/**
 * Configuration value translator definition interface
 *
 * <p>This interface defines the required methods that custom configuration translators needs to implement. It enables
 * the use of custom, domain specific object types in configuration interfaces or the customization by the project
 * on how configuration values will be translated to objects during resolution.</p>
 *
 * <p>This interface required a generic type in its definition. The generic type defines the target type of the
 * translation. In another works, a class that implements {@code ConfigurationValueTranslator<Integer>} will be
 * called to do the translation for every configuration value that uses {@link Integer} as the type.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 * @param <T> Generic
 */
public interface ConfigurationValueTranslator<T> {

    /**
     * Translates the {@link String} value to the target type compatible object
     *
     * @param value Value to be translated
     * @return Translated object
     */
    T translate(String value);

}
