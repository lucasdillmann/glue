package glue.config.api.extension;

import glue.config.api.exception.ConfigurationException;
import glue.config.api.translator.ConfigurationValueTranslator;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;
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

    private final Instance<ConfigurationValueTranslator> cdiResolver;
    private final Logger logger;

    /**
     * Package protected constructor with {@link ConfigurationValueTranslator} CDI {@link Instance} gateway
     * and {@link Logger} initialization
     *
     * @param cdiResolver CDI resolver
     * @param logger Logger instance
     */
    @Inject
    ConfigurationValueTranslatorBridge(final Instance<ConfigurationValueTranslator> cdiResolver,
                                       final Logger logger) {
        this.cdiResolver = cdiResolver;
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

        final TypeLiteral<ConfigurationValueTranslator<T>> translatorType =
                new TypeLiteral<ConfigurationValueTranslator<T>>() {
                };
        final Instance<ConfigurationValueTranslator<T>> translator = cdiResolver.select(translatorType);

        if (translator.isAmbiguous())
            throw new ConfigurationException(
                    "Multiple ConfigurationValueTraslator implementations found for " + targetType.getName() +
                            ". Please fix by removing or qualifying the ones that shouldn't be used."
            );

        return Optional.of(translator)
                .filter(instance -> !instance.isUnsatisfied())
                .map(Instance<ConfigurationValueTranslator<T>>::get)
                .orElseGet(() -> {
                    logger.debug("No custom translator available for {}. Using the default translation mechanism.", targetType.getName());
                    return configurationValue ->
                            DefaultConfigurationTranslator.forType(targetType).translate(configurationValue);
                })
                .translate(value);
    }

}
