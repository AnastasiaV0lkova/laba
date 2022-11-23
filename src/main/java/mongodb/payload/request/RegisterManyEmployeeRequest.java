package mongodb.payload.request;

import mongodb.models.ManyEmployees;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RegisterManyEmployeeRequest {

    private String username;
    private String firstName, lastName, patronymic;
    private String email;
    private Set<String> classes;
    private List<String> students1;
    private List<ManyEmployees> students;
    private Set<String> roles;
    private String password;

}
