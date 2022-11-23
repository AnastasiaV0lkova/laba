package mongodb.payload.request;

import mongodb.models.ManyStudents;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RegisterManyStudentRequest {

    private String username;
    private String firstName, lastName, patronymic;
    private String email;
    private Set<String> classes;
    private List<String> students1;
    private List<ManyStudents> students;
    private Set<String> roles;
    private String password;

}
