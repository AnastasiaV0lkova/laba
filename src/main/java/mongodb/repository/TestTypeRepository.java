package mongodb.repository;

import mongodb.models.ETestStatus;
import mongodb.models.TestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TestTypeRepository extends MongoRepository<TestStatus, String> {
    Optional<TestStatus> findByName(ETestStatus name);

    Optional<TestStatus> findById(String id);

    Boolean existsByName(String name);

}
