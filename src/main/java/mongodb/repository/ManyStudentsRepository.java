package mongodb.repository;

import mongodb.models.ListOfManyStudents;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ManyStudentsRepository extends MongoRepository<ListOfManyStudents, String> {
}
