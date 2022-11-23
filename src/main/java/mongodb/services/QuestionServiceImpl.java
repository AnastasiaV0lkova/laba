package mongodb.services;

import mongodb.models.Question;
import mongodb.models.Test;
import mongodb.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionServiceImpl implements IQuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public String deleteById(String id) {
        questionRepository.deleteById(id);
        return id;
    }

    @Override
    public Optional<Question> findById(String id) {
        return questionRepository.findById(id);
    }

    @Override
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Override
    public String deleteQuestions(Question id) {
        questionRepository.delete(id);
        return "nice deleting!";
    }

    @Override
    public Set<Question> findByTest(Test test) {
        return questionRepository.findByTest(test);
    }

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }

}
