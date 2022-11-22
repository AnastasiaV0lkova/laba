package com.alchotest.spring.jwt.mongodb.payload.request;

import com.alchotest.spring.jwt.mongodb.models.ChosenAnswer;
import lombok.Data;

import java.util.List;

@Data
public class SaveRequest {

    private String question;

    private String testId;

    private String studentId;

    private List<ChosenAnswer> questionsPassing;

    private String mark;

    private String updated;

    private String comments;
}
