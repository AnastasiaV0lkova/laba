package mongodb.controllers;

import mongodb.exception.ClassNotFoundException;
import mongodb.exception.QuestionNotFoundException;
import mongodb.exception.SubjNotFoundException;
import mongodb.models.*;
import mongodb.payload.request.TestRequest;
import mongodb.payload.response.MessageResponse;
import mongodb.repository.ChosenAnswersRepository;
import mongodb.repository.QuestionRepository;
import mongodb.repository.EmployeeRepository;
import mongodb.services.*;
import lombok.var;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final IClassesService classesService;
    private final EmployeeRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final ISubjService subjService;
    private final IQuestionService questionService;
    private final TestTypeServiceImpl testTypeRepository;
    private final S3Factory s3Factory;
    private final TestServiceImpl testService;
    private final ChosenAnswersRepository chosenAnswersRepository;

    @Autowired
    public TestController(IClassesService classesService, EmployeeRepository studentRepository, QuestionRepository questionRepository, ISubjService subjService, IQuestionService questionService, TestTypeServiceImpl TestTypeRepository, S3Factory s3Factory, TestServiceImpl testService, ChosenAnswersRepository chosenAnswersRepository) {
        this.classesService = classesService;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
        this.subjService = subjService;
        this.questionService = questionService;
        this.testTypeRepository = TestTypeRepository;
        this.s3Factory = s3Factory;
        this.testService = testService;
        this.chosenAnswersRepository = chosenAnswersRepository;
    }

    @PostMapping("/createTest")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> createTest(@Valid @RequestBody TestRequest testRequest) {

        Test test = new Test(testRequest.getName(), testRequest.getStart(), testRequest.getEnd());

        Subject subject = subjService.findById(testRequest.getSubjects()).orElseThrow(() -> new SubjNotFoundException("Error: Subject not found"));
        test.setSubject(subject);

        List<String> strClasses = testRequest.getClasses();

        assert strClasses != null;
        strClasses.forEach(c -> {
            if (Integer.parseInt(c) <= 11 && Integer.parseInt(c) >= 1) {
                List<Classes> classes = classesService.findByNumber(c);
                test.setClasses(classes);
            } else {
                throw new ClassNotFoundException("Error: Class not found");
            }

        });

        TestStatus status = testTypeRepository.findById(testRequest.getStatus()).orElseThrow(() -> new QuestionNotFoundException("Error: type not found"));
        test.setStatus(status);

        testService.save(test);
        return new ResponseEntity<>(test, HttpStatus.OK);
    }

    @GetMapping("/tests")
    public List<Test> findAll() {
        return testService.findAll();
    }

    @GetMapping("/testById/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public Optional<Test> findById(@PathVariable String id) {
        return testService.findById(id);
    }

    @DeleteMapping("deleteTest/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Optional<Test> byId = testService.findById(id);
        if (byId.isPresent()) {
            Test test = byId.get();

            Set<Question> questions = test.getQuestionList();
            for (Question question : questions) {
                String url = question.getUrl();
                if (url != null) {
                    final String regex = "[^/]{36}";
                    String deleteUrl;
                    final Pattern pattern = Pattern.compile(regex);
                    final Matcher matcher = pattern.matcher(url);
                    if (matcher.find()) {
                        deleteUrl = matcher.group(0);
                        s3Factory.deleteFileFromS3Bucket(deleteUrl + ".png");
                    }
                }
            }
            questions.forEach(questionService::deleteQuestions);
        }

        testService.deleteById(id);
        return ResponseEntity.ok(new MessageResponse(id));
    }

    @PutMapping("/updateTest/{id}")
    public Test updateTest(@PathVariable String id, @RequestBody Test test) {
        Optional<Test> byId = testService.findById(id);
        if (byId.isPresent()) {
            Test updatedTest = byId.get();

            if (test.getTestName() != null) {
                updatedTest.setTestName(test.getTestName());
            }

            if (test.getStart() != null) {
                updatedTest.setStart(test.getStart());
            }

            if (test.getEnd() != null) {
                updatedTest.setEnd(test.getEnd());
            }

            if (test.getTimePassing() != null) {
                updatedTest.setTimePassing(test.getTimePassing());
            }

            Set<String> strQuestions = test.getQuestions1();
            if (strQuestions != null) {
                Set<Question> questions = test.getQuestionList();
                strQuestions.forEach(c -> {
                    Question classes1 = questionService.findById(c).orElseThrow(() -> new QuestionNotFoundException("Error: Question not found"));
                    questions.add(classes1);
                });
                updatedTest.setQuestionList(questions);
            }

            List<String> strClasses = test.getAssign();
            if (strClasses != null) {
                List<Classes> classes = new ArrayList<>();
                strClasses.forEach(c -> {
                    Classes classes1 = classesService.findById(c).orElseThrow(() -> new QuestionNotFoundException("Error: Question not found"));
                    classes.add(classes1);
                });
                updatedTest.setClasses(classes);
            }

            if (test.getStatus1() != null) {
                TestStatus status = testTypeRepository.findById(test.getStatus1()).orElseThrow(() -> new QuestionNotFoundException("Error: type not found"));
                updatedTest.setStatus(status);
            }

            return testService.save(updatedTest);
        } else {
            throw new RuntimeException("Error: test not found!");
        }
    }

    @PutMapping("/updateStudents/{id}")
    public Test updatePassedStudents(@PathVariable String id, @RequestBody TestRequest testRequest) {
        Optional<Test> byId = testService.findById(id);
        if (byId.isPresent()) {
            Test test = byId.get();

            test.setPassed(testRequest.getStudents());

            return testService.save(test);
        } else {
            throw new RuntimeException("Error: test not found!");
        }
    }

    @PostMapping("/clone/{id}")
    public ResponseEntity<?> cloneTest(@PathVariable String id, @RequestBody TestRequest testRequest) {
        Optional<Test> test = testService.findById(id);
        if (test.isPresent()) {
            Test test1 = test.get();
            Test cloneTest = new Test();

            if (testRequest.getName() == null) {
                cloneTest.setTestName(test1.getTestName());
            }

            if (testRequest.getStart() == null) {
                cloneTest.setStart(test1.getStart());
            }

            if (testRequest.getEnd() == null) {
                cloneTest.setEnd(test1.getEnd());
            }

            if (testRequest.getTimePassing() == null) {
                cloneTest.setTimePassing(test1.getTimePassing());
            }

            if (testRequest.getClasses() == null) {
                cloneTest.setClasses(test1.getClasses());
            }

            if (testRequest.getQuestionList() == null) {
                Set<Question> questions = test1.getQuestionList();
                Set<Question> cloneQuestions = new HashSet<>();
                for (Question question : questions) {
                    Question newQuestion = new Question();
                    newQuestion.setBody(question.getBody());
                    newQuestion.setListAnswers(question.getListAnswers());
                    newQuestion.setTypes(question.getTypes());
                    String oldUrl = question.getUrl();
                    if (oldUrl != null) {
                        final String regex = "[^/]{36}";
                        final Pattern pattern = Pattern.compile(regex);
                        final Matcher matcher = pattern.matcher(oldUrl);
                        if (matcher.find()) {
                            String urlForCopy = matcher.group(0);
                            String newUrl = s3Factory.copyObject(urlForCopy);
                            newQuestion.setUrl("https://dq2g5czw4138n.cloudfront.net/questions/" + newUrl + ".png");
                        }
                    }
                    questionRepository.save(newQuestion);
                    cloneQuestions.add(newQuestion);
                }

                cloneTest.setQuestionList(cloneQuestions);

            }

            if (testRequest.getStatus() == null) {
                cloneTest.setStatus(test1.getStatus());
            }

            if (testRequest.getAssign() == null) {
                cloneTest.setAssign(test1.getAssign());
            }

            if (testRequest.getSubjects() == null) {
                cloneTest.setSubject(test1.getSubject());
            }

            testService.save(cloneTest);
            return ResponseEntity.ok(new MessageResponse("wow! U clone it!"));
        } else {
            throw new RuntimeException("Error: cant copy test");
        }

    }

    @GetMapping(value = "/bySubj/{id}/{classId}/{start}/{end}/{lang}/reporting.xlsx")
    public ResponseEntity<InputStreamResource> findBySubject(
            @PathVariable String id, @PathVariable String classId,
            @PathVariable String start,
            @PathVariable String end,
            @PathVariable String lang) throws IOException {


//        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        DateTimeFormatter dateTimeFormat = new DateTimeFormatter();
//        DateTime s = DateTime.parse(start + " 00:00:00", dateTimeFormat);
//        DateTime e = DateTime.parse(end + " 00:00:00", dateTimeFormat);

        DateTime s = DateTime.parse(start + " 00:00:00");
        DateTime e = DateTime.parse(end + " 00:00:00");
        List<Test> reporting = testService.findBySubjectAndClasses(id, classId);
        List<Test> dataReporting = new ArrayList<>();
        //create a list with test
        for (Test test : reporting) {
            if (test.getStart() != null && test.getEnd() != null) {
                if (test.getStart().isAfter(s) && test.getEnd().isBefore(e)) {
                    dataReporting.add(test);
                }
            }
        }

        //create a list with chosen questions from dataReporting
        List<List<ChosenAnswers>> chosenAnswers = new ArrayList<>();
        for (Test test : dataReporting) {
            chosenAnswers.add(chosenAnswersRepository.findByTestId(test.getId()));
        }

        List<Employee> students = studentRepository.findByClasses(classId);
        int rowCount = 0;
        var in = TestServiceImpl.testsToExcel(dataReporting, rowCount, chosenAnswers, students, lang);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporting.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}