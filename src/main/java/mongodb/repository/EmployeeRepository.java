package mongodb.repository;

import mongodb.models.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("{ 'classes' : ?0 }")
    List<Employee> findByClasses(String id);

}
