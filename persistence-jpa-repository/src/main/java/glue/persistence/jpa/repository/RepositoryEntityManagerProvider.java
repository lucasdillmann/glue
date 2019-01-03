package glue.persistence.jpa.repository;

import glue.persistence.jpa.api.EntityManagerProvider;
import glue.persistence.jpa.api.PersistenceContext;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Specialization of {@link EntityManagerProvider} compatible with Deltaspike Data
 *
 * <p>This class specializes the Glue {@link EntityManagerProvider}, producing {@link EntityManager} instances
 * under {@link TransactionScoped}. Main goal of this class is to enable Deltaspike Data APIs to work with Glue
 * JPA API, providing to users the possibility to use Deltaspike repositories and
 * {@link org.apache.deltaspike.jpa.api.transaction.Transactional} control.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-27
 */
@Default
public class RepositoryEntityManagerProvider extends EntityManagerProvider {

    /**
     * Constructor with {@link PersistenceContext} and {@link Logger} initialization
     *
     * @param context Persistence context
     * @param logger  Logger instance
     */
    @Inject
    public RepositoryEntityManagerProvider(final PersistenceContext context, final Logger logger) {
        super(context, logger);
    }

    /**
     * Produces {@link EntityManager} instances under {@link TransactionScoped}
     *
     * <p>This method produces {@link EntityManager} instances under the scope of a request (if applicable).</p>
     *
     * @return EntityManager instance
     */
    @Override
    @Produces
    @Specializes
    @TransactionScoped
    public EntityManager entityManager() {
        return super.entityManager();
    }
}
