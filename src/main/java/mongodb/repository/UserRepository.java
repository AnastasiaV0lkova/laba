package mongodb.repository;

import mongodb.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("{ 'roles' : ?0 }")
    List<User> findByRoles(String id);

    Optional<User> findByUsername(String username);

}
