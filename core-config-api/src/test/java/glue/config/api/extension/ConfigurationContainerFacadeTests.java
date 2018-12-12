package glue.config.api.extension;

import glue.config.api.translator.ConfigurationContainerTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link ConfigurationContainerFacade}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-09
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationContainerFacadeTests {

    @Mock
    private Logger logger;
    @Mock
    private Instance<ConfigurationContainerTranslator> translatorInstance;
    @Mock
    private ConfigurationContainerTranslator translator;

    private ConfigurationContainerFacade facade;

    @Before
    public void setup() {
        doReturn(translator).when(translatorInstance).get();
        doReturn(translatorInstance).when(translatorInstance).select(Matchers.any(TypeLiteral.class));
        this.facade = new ConfigurationContainerFacade(translatorInstance, logger);
    }

    @Test
    public void shouldCallTranslatorToConvertToContainerValue() {
        // scenario
        final Integer expectedValue = new Random().nextInt();
        doReturn(Optional.ofNullable(expectedValue)).when(translator).translate(expectedValue);

        // execution
        final Optional<Integer> translatedValue = facade.translate(expectedValue, Optional.class);

        // validation
        assertThat(translatedValue, is(notNullValue()));
        assertThat(translatedValue.get(), is(expectedValue));
        verify(translator, times(1)).translate(expectedValue);
    }

    @Test
    public void shouldCheckForAvailableTranslatorWhenCheckingIfIsContainer() {
        // execution
        facade.isAContainer(Optional.class);

        // validation
        verify(translatorInstance, times(1)).isUnsatisfied();
    }

    @Test
    public void shouldCallTranslatorToIdentifyTargetConfigurationType() {
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
        doReturn(expectedType).when(translator).getTargetType(parameterizedType);

        // execution
        final Class<?> actualType = facade.getTargetConfigurationValueType(Optional.class, parameterizedType);

        // validation
        assertThat(actualType, is(notNullValue()));
        assertThat(actualType, is(equalTo(expectedType)));
        verify(translator, times(1)).getTargetType(parameterizedType);
    }
}
