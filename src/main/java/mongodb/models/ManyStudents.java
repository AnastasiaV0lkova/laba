package mongodb.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ManyStudents {

    private String id;
    private String username;
    private String email;
    private String firstName, lastName, patronymic;
    private String password;
    private Set<String> classes = new HashSet<>();

    public ManyStudents(String username, String email, String firstName, String lastName, String patronymic, String password, Set<String> classes) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.password = password;
        this.classes = classes;
    }

    private String roles = "5eedfcafccf89a6e452c4ef2";
}
