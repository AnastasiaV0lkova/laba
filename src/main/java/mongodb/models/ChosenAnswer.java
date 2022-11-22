package com.alchotest.spring.jwt.mongodb.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChosenAnswer {

    private String questionId;

    private List<QuestionAnswer> question = new ArrayList<>();
}
