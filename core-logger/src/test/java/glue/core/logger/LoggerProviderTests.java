package glue.core.logger;

import org.junit.Test;
import org.slf4j.Logger;

import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.reflect.Member;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link LoggerProvider}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
public class LoggerProviderTests {

    @Test
    public void shouldProduceALogger() {
        // scenario
        final InjectionPoint injectionPoint = mock(InjectionPoint.class);
        final Member member = mock(Member.class);
        class TestClass {}
        doReturn(member).when(injectionPoint).getMember();
        doReturn(TestClass.class).when(member).getDeclaringClass();

        // execution
        final Logger logger = new LoggerProvider().logger(injectionPoint);

        // validation
        assertThat(logger, is(not(nullValue())));
        verify(injectionPoint, times(1)).getMember();
        verify(member, times(1)).getDeclaringClass();
    }

}
