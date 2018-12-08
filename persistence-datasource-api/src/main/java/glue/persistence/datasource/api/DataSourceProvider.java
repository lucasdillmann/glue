package glue.persistence.datasource.api;

import javax.sql.DataSource;

/**
 * JDBC DataSource provider interface
 *
 * <p>This interface defines the required methods that any {@link DataSource} implementation needs to provide
 * to be able to integrate with Glue API. By using this interface any API consumer class can easily create {@link DataSource}
 * instances to work with.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-08
 */
public interface DataSourceProvider {

    /**
     * Produces a {@link DataSource} instance for the given {@link ConnectionProperties}
     *
     * @param connectionProperties Connection properties
     * @return Produced {@link DataSource}
     */
    DataSource produce(ConnectionProperties connectionProperties);

}
