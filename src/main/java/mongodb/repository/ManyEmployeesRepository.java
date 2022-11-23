package mongodb.repository;

import mongodb.models.ListOfManyEmployees;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ManyEmployeesRepository extends MongoRepository<ListOfManyEmployees, String> {
}
