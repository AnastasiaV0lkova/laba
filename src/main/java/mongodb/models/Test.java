package com.alchotest.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;


@Document(collection = "test")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Test {
    @Id
    private String id;

    @DBRef
    private TestStatus status;
    private String status1;

    @Size(max = 3)
    private String timePassing;

    @DBRef
    private List<Classes> classes = new ArrayList<>();
    private List<String> assign;

    @DBRef
    private Subject subject;
    private String subject1;

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")     "start":"2020-09-22T22:08:28"
    private DateTime start;
    private DateTime end;

    private List<PassedStudents> passed = new ArrayList<>();

    @DBRef
    private Set<Question> questionList = new HashSet<>();
    private Set<String> questions1;

    @NotBlank
    @Size(max = 120)
    private String testName;

    @CreatedDate
    private Date creationDate;

    @LastModifiedDate
    private Date updateDate;

    public Test(String testName, DateTime start, DateTime end) {
        this.testName = testName;
        this.start = start;
        this.end = end;
    }
}
