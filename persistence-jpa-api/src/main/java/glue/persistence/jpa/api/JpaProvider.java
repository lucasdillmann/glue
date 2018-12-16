package glue.persistence.jpa.api;

import javax.persistence.EntityManagerFactory;

/**
 * JPA Provider definition interface
 *
 * <p>This interface defines the required methods that any JPA implementation needs to provide to integrate himself
 * with Glue. In another words this interface acts as a gateway between Glue and the JPA implementation by
 * providing access to {@link EntityManagerFactory}.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-07
 */
public interface JpaProvider {

    /**
     * Creates and returns an {@link EntityManagerFactory} instance
     *
     * @return Entity manager factory
     */
    EntityManagerFactory buildFactory();
}
