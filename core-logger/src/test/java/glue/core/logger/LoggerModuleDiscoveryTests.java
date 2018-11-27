package glue.core.logger;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test cases for {@link LoggerModuleDiscovery}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
public class LoggerModuleDiscoveryTests {

    @Test
    public void shouldReturnModuleBasePackage() {
        // scenario
        final Package expectedPackage = getClass().getPackage();

        // execution
        final Package returnedPackage = new LoggerModuleDiscovery().getPackage();

        // validation
        assertThat(returnedPackage, is(not(nullValue())));
        assertThat(returnedPackage.getName(), is(expectedPackage.getName()));
    }

}
