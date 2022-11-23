package mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "question")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Question {
    @Id
    private String id;

    @NotBlank
    private String body;

    private String url;

    @DBRef
    private Test test;

    @DBRef
    private QuestionType types;
    private String type1;

    private List<QuestionAnswer> listAnswers = new ArrayList<>();

    public Question(String body, List<QuestionAnswer> listAnswers, String url) {
        this.body = body;
        this.listAnswers = listAnswers;
        this.url = url;
    }

}
