package mongodb.repository;

import mongodb.models.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TestRepository extends MongoRepository<Test, String> {

    Optional<Test> findById(String id);

    Boolean existsByTestName(String name);

    List<Test> findBySubjectAndClasses(String subject, String classes);

    @Query("{ 'start' : ?0 }")
    Optional<Test> findByStart(LocalDateTime start);
}
