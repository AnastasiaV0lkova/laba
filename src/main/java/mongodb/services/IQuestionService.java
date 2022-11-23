package mongodb.services;

import mongodb.models.Question;
import mongodb.models.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IQuestionService {

    String deleteById(String id);

    Optional<Question> findById(String id);

    List<Question> findAll();

    String deleteQuestions(Question id);

    Set<Question> findByTest(Test test);

    Question save(Question question);
}
