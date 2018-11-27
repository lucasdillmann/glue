package glue.core.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Test cases for {@link ExceptionUtils}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
public class ExceptionUtilsTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldRetrowCheckedExceptionAsUnckecked() {
        final SQLException exception = new SQLException();

        expectedException.expect(equalTo(exception));
        new ExceptionUtils().rethrowAsUnchecked(exception);
    }

}
