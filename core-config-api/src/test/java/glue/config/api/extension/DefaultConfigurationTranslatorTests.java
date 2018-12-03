package glue.config.api.extension;

import glue.config.api.exception.ConfigurationException;
import glue.config.api.extension.artifacts.DefaultConfigurationTranslatorArtifacts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Test cases for {@link DefaultConfigurationTranslator}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
public class DefaultConfigurationTranslatorTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldTranslateEnum() {
        // scenario
        final String enumValue = DefaultConfigurationTranslatorArtifacts.Enum.A.name();
        final DefaultConfigurationTranslatorArtifacts.Enum expectedValue = DefaultConfigurationTranslatorArtifacts.Enum.A;

        // execution
        final DefaultConfigurationTranslatorArtifacts.Enum actualValue = DefaultConfigurationTranslator
                .forType(DefaultConfigurationTranslatorArtifacts.Enum.class)
                .translate(enumValue);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue, is(expectedValue));
    }

    @Test
    public void shouldTranslateUsingConstructor() {
        // scenario
        final String input = "123";
        final Integer expectedValue = 123;

        // execution
        final DefaultConfigurationTranslatorArtifacts.Constructor actualValue = DefaultConfigurationTranslator
                .forType(DefaultConfigurationTranslatorArtifacts.Constructor.class)
                .translate(input);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue.getValue(), is(expectedValue));
    }

    @Test
    public void shouldTranslateUsingValueOf() {
        // scenario
        final String input = "123";
        final Integer expectedValue = 123;

        // execution
        final DefaultConfigurationTranslatorArtifacts.ValueOf actualValue = DefaultConfigurationTranslator
                .forType(DefaultConfigurationTranslatorArtifacts.ValueOf.class)
                .translate(input);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue.getValue(), is(expectedValue));
    }

    @Test
    public void shouldTranslateUsingFromString() {
        // scenario
        final String input = "123";
        final Integer expectedValue = 123;

        // execution
        final DefaultConfigurationTranslatorArtifacts.FromString actualValue = DefaultConfigurationTranslator
                .forType(DefaultConfigurationTranslatorArtifacts.FromString.class)
                .translate(input);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue.getValue(), is(expectedValue));
    }

    @Test
    public void shouldTranslateUsingParse() {
        // scenario
        final String input = "123";
        final Integer expectedValue = 123;

        // execution
        final DefaultConfigurationTranslatorArtifacts.Parse actualValue = DefaultConfigurationTranslator
                .forType(DefaultConfigurationTranslatorArtifacts.Parse.class)
                .translate(input);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue.getValue(), is(expectedValue));
    }

    @Test
    public void shouldTranslateUsingParseString() {
        // scenario
        final String input = "123";
        final Integer expectedValue = 123;

        // execution
        final DefaultConfigurationTranslatorArtifacts.ParseString actualValue = DefaultConfigurationTranslator
                .forType(DefaultConfigurationTranslatorArtifacts.ParseString.class)
                .translate(input);

        // validation
        assertNotNull(actualValue);
        assertThat(actualValue.getValue(), is(expectedValue));
    }

    @Test
    public void shouldThrowExceptionWhenAutomaticTranslationIsntPossible() {
        // scenario
        final String className = DefaultConfigurationTranslatorArtifacts.Uncompatible.class.getName();
        final String input = Double.toString(new Random().nextDouble());
        final String expectedMessage = "Configuration value '" + input + "' can't be automatically translated to " +
                className + " using default translation mechanism. Please implement a custom " +
                "ConfigurationValueTranslator for the type to solve this.";
        expectedException.expect(ConfigurationException.class);
        expectedException.expectMessage(equalTo(expectedMessage));

        // execution
        DefaultConfigurationTranslator
                .forType(DefaultConfigurationTranslatorArtifacts.Uncompatible.class)
                .translate(input);
    }

}
