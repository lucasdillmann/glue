package glue.test.test;

import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Default
public class TestService {

    private final TestRepository repository;
    private final Logger logger;

    @Inject
    public TestService(final TestRepository repository, final Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public Optional<TestEntity> getById(final UUID id) {
        Objects.requireNonNull(id);
        logger.debug("Retrieving {} with ID {}", TestEntity.class.getName(), id);
        return repository.findOptionalBy(id);
    }

    public boolean exists(final TestEntity entity) {
        if (entity == null || entity.getId() == null)
            return false;

        return repository.findOptionalBy(entity.getId()).isPresent();
    }

    public TestEntity save(final TestEntity testEntity) {
        Objects.requireNonNull(testEntity);

        if (testEntity.getId() == null) {
            testEntity.setId(UUID.randomUUID());
        }

        logger.debug("Saving {} with ID {}", TestEntity.class.getName(), testEntity.getId());
        return repository.save(testEntity);
    }

    public void delete(final TestEntity testEntity) {
        Objects.requireNonNull(testEntity);

        logger.debug("Removing {} with ID {}", TestEntity.class.getName(), testEntity.getId());
        repository.remove(testEntity);
    }

    public List<TestEntity> getAll() {
        logger.debug("Retrieving all {}", TestEntity.class.getName());
        return repository.findAll();
    }
}
