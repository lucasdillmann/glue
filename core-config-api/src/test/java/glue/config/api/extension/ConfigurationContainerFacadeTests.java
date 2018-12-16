package glue.config.api.extension;

import glue.config.api.translator.ConfigurationContainerTranslator;
import glue.core.util.CdiUtils;
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
import static org.mockito.Mockito.any;
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
    private CdiUtils cdiUtils;
    @Mock
    private ConfigurationContainerTranslator translator;

    private ConfigurationContainerFacade facade;

    @Before
    public void setup() {
        doReturn(Optional.of(translator)).when(cdiUtils).getTypedBean(any(), any(), any());
        this.facade = new ConfigurationContainerFacade(cdiUtils, logger);
    }

    @Test
    public void shouldCallTranslatorToConvertToContainerValue() {
        // scenario
        final Integer expectedValue = new Random().nextInt();
        doReturn(Optional.ofNullable(expectedValue)).when(translator).translate(expectedValue);
        doReturn(Optional.of(translator)).when(cdiUtils).getTypedBean(ConfigurationContainerTranslator.class, Optional.class);

        // execution
        final Optional<Integer> translatedValue = facade.translate(expectedValue, Optional.class);

        // validation
        assertThat(translatedValue, is(notNullValue()));
        assertThat(translatedValue.get(), is(expectedValue));
        verify(translator, times(1)).translate(expectedValue);
    }

    @Test
    public void shouldCheckForAvailableTranslatorWhenCheckingIfIsContainer() {
        // scenario
        doReturn(Optional.empty()).when(cdiUtils).getTypedBean(ConfigurationContainerTranslator.class, Optional.class);

        // execution
        facade.isAContainer(Optional.class);

        // validation
        verify(cdiUtils, times(1)).getTypedBean(ConfigurationContainerTranslator.class, Optional.class);
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
        doReturn(Optional.of(translator)).when(cdiUtils).getTypedBean(ConfigurationContainerTranslator.class, Optional.class);

        // execution
        final Class<?> actualType = facade.getTargetConfigurationValueType(Optional.class, parameterizedType);

        // validation
        assertThat(actualType, is(notNullValue()));
        assertThat(actualType, is(equalTo(expectedType)));
        verify(translator, times(1)).getTargetType(parameterizedType);
    }
}
