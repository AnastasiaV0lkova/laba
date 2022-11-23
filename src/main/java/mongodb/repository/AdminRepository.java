package mongodb.repository;

import mongodb.models.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
