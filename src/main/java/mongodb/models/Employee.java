package mongodb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Data
@Document(collection = "users")
@NoArgsConstructor
public class Employee {
    @Id
    private String id;

    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
    @Size(min = 4, max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 60)
    private String firstName, lastName, patronymic;

    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    @DBRef
    private Set<Classes> classes = new HashSet<>();
    private Set<String> classes2;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public Employee(String username, String email, String firstName, String lastName, String patronymic, String password) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.password = password;
    }

}
