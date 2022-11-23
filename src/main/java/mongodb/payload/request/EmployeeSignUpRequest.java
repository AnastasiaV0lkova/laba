package mongodb.payload.request;

import mongodb.models.Classes;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class EmployeeSignUpRequest {
    @NotBlank
    @Size(min = 3, max = 60)
    private String username;

    @Id
    private String id;

    @NotBlank
    @Size(max = 60)
    private String firstName, lastName, patronymic;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    private Set<String> roles;
    private Set<String> classes;
    private String classes1;
    private Classes classes3;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

}
