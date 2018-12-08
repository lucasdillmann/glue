package glue.persistence.datasource.api;

import java.sql.Driver;

/**
 * DataSource connection properties bean
 *
 * <p>This class holds database connection properties for {@link javax.sql.DataSource} creation requests.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-08
 */
public final class ConnectionProperties {

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String schema;
    private final Class<? extends Driver> driverClass;

    /**
     * Constructor with all properties initialization
     *
     * @param jdbcUrl JDBC connection URL
     * @param username Authentication username
     * @param password Authentication password
     * @param schema Database schema
     * @param driverClass JDBC driver class
     */
    public ConnectionProperties(final String jdbcUrl,
                                final String username,
                                final String password,
                                final String schema,
                                final Class<? extends Driver> driverClass) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.schema = schema;
        this.driverClass = driverClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }

    public Class<? extends Driver> getDriverClass() {
        return driverClass;
    }
}
