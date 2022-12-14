package mongodb.repository;

import mongodb.models.ERole;
import mongodb.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
  Optional<Role> findById(String id);
}
