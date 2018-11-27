package glue.webcontainer.api;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test cases for {@link WebContainerModuleDiscovery}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
public class WebContainerModuleDiscoveryTests {

    @Test
    public void shouldReturnModuleBasePackage() {
        // scenario
        final Package expectedPackage = getClass().getPackage();

        // execution
        final Package returnedPackage = new WebContainerModuleDiscovery().getPackage();

        // validation
        assertThat(returnedPackage, is(not(nullValue())));
        assertThat(returnedPackage.getName(), is(expectedPackage.getName()));
    }

}
