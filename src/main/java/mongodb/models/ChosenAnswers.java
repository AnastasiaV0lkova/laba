package mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "chosen_question")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChosenAnswers {

    @Id
    private String id;

    @DBRef
    private Test testId;

    private String mark;

    private String updated;

    private String comments;

    @DBRef
    private Student studentId;

    private List<ChosenAnswer> questionsPassing = new ArrayList<>();

    public ChosenAnswers(List<ChosenAnswer> questionsPassing) {
        this.questionsPassing = questionsPassing;
    }
}
