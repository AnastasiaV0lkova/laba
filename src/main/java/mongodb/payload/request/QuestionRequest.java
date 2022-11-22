package com.alchotest.spring.jwt.mongodb.payload.request;

import com.alchotest.spring.jwt.mongodb.models.QuestionAnswer;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
public class QuestionRequest {

    private String id;

    @NotBlank
    private String body;

    private String answer;
    private boolean correct;
    private boolean chosen = false;
    private String url;

    private Set<String> files;
    private String types;
    private String subjects;
    private String test;
//    private Set<String> types;
//    private Set<String> subjects;
    private List<QuestionAnswer> listAnswers;

}
