package glue.config.api.extension;

import glue.config.api.extension.artifacts.ConfigurationMetadataArtifact;
import glue.core.util.ExceptionUtils;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Test cases for {@link ConfigurationMetadata}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
public class ConfigurationMetadataTests {

    @Test
    public void shouldReturnFullKeyUsingNameFromAnnotation() {
        // scenario
        final Method method = getMethod("returnsDoubleWithAnnotationAndKeyAsTest1");
        final ConfigurationMetadata metadata = new ConfigurationMetadata(method);
        final String expectedKey = "testArtifact.test1";

        // execution
        final String actualKey = metadata.getKey();

        // validation
        assertThat(actualKey, is(expectedKey));
    }

    @Test
    public void shouldReturnFullKeyUsingMethodName() {
        // scenario
        final Method method = getMethod("returnsIntegerWithoutAnnotation");
        final ConfigurationMetadata metadata = new ConfigurationMetadata(method);
        final String expectedKey = "testArtifact.returnsIntegerWithoutAnnotation";

        // execution
        final String actualKey = metadata.getKey();

        // validation
        assertThat(actualKey, is(expectedKey));
    }

    @Test
    public void shouldReturnDefaultValue() {
        // scenario
        final Method method = getMethod("returnsStringWithAnnotationAndKeyAsTest2AndDefaultValueAsTest");
        final ConfigurationMetadata metadata = new ConfigurationMetadata(method);
        final String expectedDefault = "test";

        // execution
        final String actualDefault = metadata.getDefaultValue();

        // validation
        assertThat(actualDefault, is(expectedDefault));
    }

    @Test
    public void shouldReturnTargetType() {
        // scenario
        final Method method = getMethod("returnsIntegerWithoutAnnotation");
        final ConfigurationMetadata metadata = new ConfigurationMetadata(method);
        final Class<?> expectedType = Integer.class;

        // execution
        final Class<?> actualType = metadata.getTargetType();

        // validation
        assertNotNull(actualType);
        assertThat(actualType.getName(), is(expectedType.getName()));
    }

    private Method getMethod(String name) {
        try {
            return ConfigurationMetadataArtifact.class.getMethod(name);
        } catch (Exception ex) {
            new ExceptionUtils().rethrowAsUnchecked(ex);
            return null;
        }
    }

}
