package mongodb.services;

import mongodb.models.ChosenAnswers;
import mongodb.models.Role;
import mongodb.models.Employee;
import mongodb.models.Test;
import mongodb.repository.TestRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TestServiceImpl implements ITestService {
    private final TestRepository testRepository;

    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public Test save(Test test) {
        return testRepository.save(test);
    }

    @Override
    public void deleteById(String id) {
        testRepository.deleteById(id);
    }

    @Override
    public List<Test> findAll() {
        return testRepository.findAll();
    }

    @Override
    public Optional<Test> findById(String id) {
        return testRepository.findById(id);
    }

    @Override
    public List<Test> findBySubjectAndClasses(String subject, String classes) {
        return testRepository.findBySubjectAndClasses(subject, classes);
    }

    public static void createListForTests(Test test, Row row) { // creating cells for each row
        Cell cell;

        cell = row.createCell(1);
        if (test.getTestName() != null) {
            cell.setCellValue(test.getTestName());
        } else {
            cell.setCellValue(" ");
        }

        cell = row.createCell(2);
        cell.setCellValue(test.getStatus().getName().toString());

        cell = row.createCell(3);
        if (test.getTimePassing() != null) {
            cell.setCellValue(test.getTimePassing());
        } else {
            cell.setCellValue(" ");
        }

        cell = row.createCell(4);
        if (test.getSubject() != null) {
            cell.setCellValue(test.getSubject().getName());
        } else {
            cell.setCellValue(" ");
        }

        cell = row.createCell(5);
        if (test.getStart() != null) {
            cell.setCellValue(test.getStart().toString());
        } else {
            cell.setCellValue(" ");
        }

        cell = row.createCell(6);
        if (test.getEnd() != null) {
            cell.setCellValue(test.getEnd().toString());
        } else {
            cell.setCellValue(" ");
        }
    }

    public static void createListForChosenQuestions(Employee student, Row row, List<List<ChosenAnswers>> ch, Test test) {
        Cell cell;
        Set<Role> roles = student.getRoles();

        for (Role role : roles) {
            int count = 0;
            if (role.getId().equals("5eedfcafccf89a6e452c4ef2")) {
                cell = row.createCell(++count);
                if (student.getFirstName() != null) {
                    cell.setCellValue(student.getFirstName());
                }

                cell = row.createCell(++count);
                if (student.getLastName() != null) {
                    cell.setCellValue(student.getLastName());
                }

                cell = row.createCell(++count);
                for (List<ChosenAnswers> chosenAnswers : ch) {
                    for (ChosenAnswers chosenAnswer : chosenAnswers) {
                        if (chosenAnswer.getStudentId().getFirstName().equals(student.getFirstName())) {
                            if (chosenAnswer.getTestId().getId().equals(test.getId())) {
                                cell.setCellValue(chosenAnswer.getMark());
                            }
                        }
                    }
                }
            }else {
                --count;
            }
        }
    }

    public static void createHeaderForTestEng(Sheet sheet, String lang) {
        Row headerRow = sheet.createRow(0);
        if (lang.equals("en")) {
            headerRow.createCell(1).setCellValue("Test name");
            headerRow.createCell(2).setCellValue("Test status");
            headerRow.createCell(3).setCellValue("Time passing");
            headerRow.createCell(4).setCellValue("Test subject");
            headerRow.createCell(5).setCellValue("Test start");
            headerRow.createCell(6).setCellValue("Test end");
        } else if (lang.equals("ru")) {
            headerRow.createCell(1).setCellValue("Имя теста");
            headerRow.createCell(2).setCellValue("Статус теста");
            headerRow.createCell(3).setCellValue("Время сдачи");
            headerRow.createCell(4).setCellValue("Предмет");
            headerRow.createCell(5).setCellValue("Начало теста");
            headerRow.createCell(6).setCellValue("Конец теста");
        }

    }

    private static void createHeaderForChosenQuestionsEng(Sheet sheet, int i, String lang) {
        Row headerRow = sheet.createRow(i);
        if (lang.equals("en")) {
            headerRow.createCell(1).setCellValue("First name");
            headerRow.createCell(2).setCellValue("Last name");
            headerRow.createCell(3).setCellValue("Mark");
        } else if (lang.equals("ru")) {
            headerRow.createCell(1).setCellValue("Имя");
            headerRow.createCell(2).setCellValue("Фамилия");
            headerRow.createCell(3).setCellValue("Оценка");
        }
    }

    public static ByteArrayInputStream testsToExcel(List<Test> tests, int rowCount, List<List<ChosenAnswers>> chosenAnswers, List<Employee> students, String lang) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Reporting");

            TestServiceImpl.createHeaderForTestEng(sheet, lang);

            for (Test test : tests) {
                Row row = sheet.createRow(++rowCount);
                TestServiceImpl.createListForTests(test, row);
                rowCount++;
                createHeaderForChosenQuestionsEng(sheet, rowCount, lang);

                for (Employee student : students) {
                    Row row1 = sheet.createRow(++rowCount);
                    TestServiceImpl.createListForChosenQuestions(student, row1, chosenAnswers, test);
                    
                }

                rowCount++;
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
