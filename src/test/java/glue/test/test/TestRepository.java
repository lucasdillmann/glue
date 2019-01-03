package glue.test.test;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface TestRepository extends EntityRepository<TestEntity, UUID> {
}
