package glue.config.api.extension.artifacts;

import glue.config.api.annotation.ConfigurationInterface;
import glue.config.api.annotation.ConfigurationProperty;

/**
 * Test artifact for {@link glue.config.api.extension.ConfigurationMetadataTests}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
@ConfigurationInterface(prefix = "testArtifact.")
public interface ConfigurationMetadataArtifact {

    Integer returnsIntegerWithoutAnnotation();

    @ConfigurationProperty(key = "test1")
    Double returnsDoubleWithAnnotationAndKeyAsTest1();

    @ConfigurationProperty(key = "test2", defaultValue = "test")
    String returnsStringWithAnnotationAndKeyAsTest2AndDefaultValueAsTest();
}
