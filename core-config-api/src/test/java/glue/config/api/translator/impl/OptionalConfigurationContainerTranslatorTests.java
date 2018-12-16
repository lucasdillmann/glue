package glue.config.api.translator.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test cases for {@link OptionalConfigurationContainerTranslator}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-09
 */
@RunWith(MockitoJUnitRunner.class)
public class OptionalConfigurationContainerTranslatorTests {

    @Mock
    private Logger logger;

    private OptionalConfigurationContainerTranslator translator;

    @Before
    public void setUp() throws Exception {
        this.translator = new OptionalConfigurationContainerTranslator(logger);
    }

    @Test
    public void shouldTranslateAValueToOptional() {
        // scenario
        final String expectedValue = Integer.toString(new Random().nextInt());

        // execution
        final Optional<String> translatedValue = translator.translate(expectedValue);

        // validation
        assertThat(translatedValue, is(notNullValue()));
        assertThat(translatedValue.isPresent(), is(true));
        assertThat(translatedValue.get(), is(expectedValue));
    }

    @Test
    public void shouldExtractOptionalGenericType() {
        // scenario
        final Class<?> expectedType = Integer.class;
        final ParameterizedType parameterizedType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Class[]{expectedType};
            }

            @Override
            public Type getRawType() {
                return Optional.class;
            }

            @Override
            public Type getOwnerType() {
                return Optional.class;
            }
        };

        // execution
        final Class<?> actualType = translator.getTargetType(parameterizedType);

        // validation
        assertThat(actualType, is(notNullValue()));
        assertThat(actualType, is(equalTo(expectedType)));
    }
}
