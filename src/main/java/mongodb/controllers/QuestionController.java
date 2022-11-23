package mongodb.controllers;

import mongodb.exception.QuestionNotFoundException;
import mongodb.exception.UserNotFoundException;
import mongodb.models.*;
import mongodb.payload.request.QuestionRequest;
import mongodb.payload.request.SaveRequest;
import mongodb.payload.response.MessageResponse;
import mongodb.repository.*;
import mongodb.services.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final QuestionServiceImpl questionService;
    private final QuestionTypeRepository questionTypeRepository;
    private final EmployeeRepository studentRepository;
    private final TestRepository testRepository;
    private final ChosenAnswersRepository chosenAnswersRepository;

    @Autowired
    public QuestionController(QuestionRepository questionRepository, QuestionServiceImpl questionService, QuestionTypeRepository questionTypeRepository, TestRepository testRepository, EmployeeRepository studentRepository, ChosenAnswersRepository chosenAnswersRepository) {
        this.questionRepository = questionRepository;
        this.questionService = questionService;
        this.questionTypeRepository = questionTypeRepository;
        this.studentRepository = studentRepository;
        this.testRepository = testRepository;
        this.chosenAnswersRepository = chosenAnswersRepository;
    }

    @PostMapping(path = "/createQuestion")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionRequest questionRequest) {

        Question question = new Question(
                questionRequest.getBody(),
                questionRequest.getListAnswers(),
                questionRequest.getUrl()
        );

        QuestionType questionType = questionTypeRepository.findById(questionRequest.getTypes()).orElseThrow(() -> new QuestionNotFoundException("Error: type not found"));
        question.setTypes(questionType);

        questionRepository.save(question);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @GetMapping("/questions")
    public List<Question> allQuestions() {
        return questionService.findAll();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String id) {
        questionService.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Question deleted"));
    }

    @GetMapping("/qById/{id}")
    public Optional<Question> findById(@PathVariable String id) {
        return questionRepository.findById(id);
    }

    @PutMapping("/update/{id}")
    public Question updateQ(@PathVariable String id, @RequestBody Question question) {
        Optional<Question> byId = questionService.findById(id);
        if (byId.isPresent()) {
            Question updatedQuestion = byId.get();

            if (question.getBody() != null) {
                updatedQuestion.setBody(question.getBody());
            }

            if (question.getUrl() != null) {
                updatedQuestion.setUrl(question.getUrl());
            }

            if (question.getListAnswers() != null) {
                updatedQuestion.setListAnswers(question.getListAnswers());
            }

            if (question.getType1() != null) {
                QuestionType questionType = questionTypeRepository.findById(question.getType1()).orElseThrow(() -> new QuestionNotFoundException("Error: type not found"));
                updatedQuestion.setTypes(questionType);
            }

            return questionRepository.save(updatedQuestion);
        } else {
            throw new RuntimeException("question not found");
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveAnswers(@RequestBody SaveRequest saveRequest) {

        ChosenAnswers chosenAnswers = new ChosenAnswers(saveRequest.getQuestionsPassing());

        Test testId = testRepository.findById(saveRequest.getTestId()).orElseThrow(() -> new QuestionNotFoundException("Error: Test not found!"));
        chosenAnswers.setTestId(testId);

        Employee studentId = studentRepository.findById(saveRequest.getStudentId()).orElseThrow(() -> new UserNotFoundException("Error: Student id not found!"));
        chosenAnswers.setStudentId(studentId);

        chosenAnswersRepository.save(chosenAnswers);

        return ResponseEntity.ok(new MessageResponse(chosenAnswers.getId()));
    }

    @PutMapping("/updatedTeacher/{id}")
    public ChosenAnswers saveUpdatedTeacherForAnswers(@PathVariable String id, @RequestBody SaveRequest saveRequest) {
        Optional<ChosenAnswers> chosenAnswer = chosenAnswersRepository.findById(id);
        if (chosenAnswer.isPresent()) {
            ChosenAnswers chosenAnswers = chosenAnswer.get();
            chosenAnswers.setUpdated(saveRequest.getUpdated());
            chosenAnswers.setMark(saveRequest.getMark());
            chosenAnswers.setComments(saveRequest.getComments());
            return chosenAnswersRepository.save(chosenAnswers);
        } else {
            throw new RuntimeException("Error");
        }
    }

    @GetMapping("/allAnswers")
    public List<ChosenAnswers> findAllAnswers() {
        return chosenAnswersRepository.findAll();
    }

    @GetMapping("/answersByTest/{id}")
    public List<ChosenAnswers> findAllAnswers(@PathVariable String id) {
        return chosenAnswersRepository.findByTestId(id);
    }

    @GetMapping("/answersByTestAndStudent/{testId}/{studentId}")
    public List<ChosenAnswers> findAllAnswers1(@PathVariable String testId, @PathVariable String studentId) {
        return chosenAnswersRepository.findByTestIdAndStudentId(testId, studentId);
    }
}
