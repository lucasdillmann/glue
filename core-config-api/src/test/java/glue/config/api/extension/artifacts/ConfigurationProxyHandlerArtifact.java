package glue.config.api.extension.artifacts;

import glue.config.api.annotation.ConfigurationInterface;
import glue.config.api.annotation.ConfigurationProperty;

import java.util.Optional;

/**
 * Test artifact for {@link glue.config.api.extension.ConfigurationProxyHandlerTests}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@ConfigurationInterface(prefix = "testArtifact.")
public interface ConfigurationProxyHandlerArtifact {

    @ConfigurationProperty(key = "test")
    Integer getTest();

    @ConfigurationProperty(key = "test")
    Optional<Integer> getTestWithOptional();

    default Integer getDefaultValue() {
        return 123;
    }
}
