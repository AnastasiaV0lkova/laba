package mongodb.repository;

import mongodb.models.Classes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClassesRepository extends MongoRepository<Classes, String> {
    Optional<Classes> findById(String id);

    Boolean existsByNumberAndName(String number, String name);

    @Query("{ 'number' : ?0 }")
    List<Classes> findByNumber(String number);

    @Query("{ 'name' : ?0 }")
    List<Classes> findByName(String name);
}
