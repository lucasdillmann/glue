package glue.config.api.extension.artifacts;

import glue.config.api.annotation.ConfigurationInterface;
import glue.config.api.annotation.ConfigurationProperty;

/**
 * Test artifact for {@link glue.config.api.extension.ConfigurationProxyFactoryTests}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
public final class ConfigurationProxyFactoryArtifact {

    @ConfigurationInterface(prefix = "testArtifact.")
    public interface Valid {
        @ConfigurationProperty(key = "test", defaultValue = "1234")
        Integer getTest();
    }

    public interface WithoutAnnotation {
        Integer getTest();
    }

    public static class NotAInterface {
    }

}
