package glue.config.api.extension;

import glue.config.api.exception.ConfigurationException;
import glue.config.api.translator.ConfigurationValueTranslator;
import glue.core.util.CdiUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Configuration value translation bridge
 *
 * <p>This class acts as a bridge between the {@link ConfigurationValueTranslator} implementations from project
 * and configuration API module. It main goal is to handle the translation requests, calling {@link ConfigurationValueTranslator}
 * implementation when is available or {@link DefaultConfigurationTranslator} in all other scenarions.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@Singleton
class ConfigurationValueTranslatorBridge {

    private final CdiUtils cdiUtils;
    private final Logger logger;

    /**
     * Package protected constructor with {@link CdiUtils} and {@link Logger} initialization
     *
     * @param cdiUtils CDI resolver
     * @param logger Logger instance
     */
    @Inject
    ConfigurationValueTranslatorBridge(final CdiUtils cdiUtils,
                                       final Logger logger) {
        this.cdiUtils = cdiUtils;
        this.logger = logger;
    }

    /**
     * Translates the configuration value to the requested target type
     *
     * <p>This methods translates the requested configuration value into the target type. When available,
     * compatible {@link ConfigurationValueTranslator} instances will be used, or {@link DefaultConfigurationTranslator}
     *  will be used instead.</p>
     *
     * @param value Value to be translated
     * @param targetType Target type of the translation
     * @param <T> Generic type of the target type
     * @return Translated value
     * @throws ConfigurationException when multiple {@link ConfigurationValueTranslator} compatible implementations
     * is found
     */
    <T> T translate(final String value, final Class<T> targetType) {

        logger.debug("Translating configuration value '{}' to {}", value, targetType.getName());

        final Optional<ConfigurationValueTranslator<T>> translator = cdiUtils
                .getTypedBean(ConfigurationValueTranslator.class, targetType);

        return translator
                .orElseGet(() -> {
                    logger.debug("No custom translator available for {}. Using the default translation mechanism.", targetType.getName());
                    return configurationValue ->
                            DefaultConfigurationTranslator.forType(targetType).translate(configurationValue);
                })
                .translate(value);
    }

}
