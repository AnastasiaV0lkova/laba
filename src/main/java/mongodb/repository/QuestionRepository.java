package mongodb.repository;

import mongodb.models.Question;
import mongodb.models.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;

public interface QuestionRepository extends MongoRepository<Question, String> {
    Optional<Question> findById(String id);

    Set<Question> findByTest(Test test);

//    Optional<Question> deleteQuestions(Question question);

}
