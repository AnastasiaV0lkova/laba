package mongodb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
@NoArgsConstructor
public class ListOfManyEmployees {

    private List<ManyEmployees> manyStudents = new ArrayList<>();

    public ListOfManyEmployees(List<ManyEmployees> manyStudents) {
        this.manyStudents = manyStudents;
    }
}
