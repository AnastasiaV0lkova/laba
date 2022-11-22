package com.alchotest.spring.jwt.mongodb.payload.request;

import com.alchotest.spring.jwt.mongodb.models.ETestStatus;
import com.alchotest.spring.jwt.mongodb.models.PassedStudents;
import com.alchotest.spring.jwt.mongodb.models.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestRequest {
    @NotBlank
    @Size(min = 3, max = 60)
    private String name;
    private DateTime start;
    private DateTime end;
    private String timePassing;

    private ETestStatus eStatus;
    private String status;
    private String number;
    private String subjects;
    private Set<String> questions;
    private List<String> classes;

    private List<PassedStudents> students;
    private Set<Question> questionList;
    private List<String> assign;


}
