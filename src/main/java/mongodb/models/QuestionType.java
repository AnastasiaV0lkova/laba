package mongodb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "types")
@Data
@NoArgsConstructor
public class QuestionType {
    @Id
    private String id;

    private EQuestion name;
}
