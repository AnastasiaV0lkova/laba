package mongodb.controllers;

import mongodb.exception.BadRoleException;
import mongodb.exception.UserNotFoundException;
import mongodb.models.*;
import mongodb.payload.request.RegisterManyEmployeeRequest;
import mongodb.payload.request.SignUpRequest;
import mongodb.payload.response.MessageResponse;
import mongodb.repository.ManyEmployeesRepository;
import mongodb.repository.RoleRepository;
import mongodb.repository.EmployeeRepository;
import mongodb.services.IClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import mongodb.exception.ClassNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployeeController {

    private final EmployeeRepository studentRepository;
    private final RoleRepository roleRepository;
    private final IClassesService classesService;
    private final PasswordEncoder encoder;
    private final ManyEmployeesRepository manyStudentsRepository;

    @Autowired
    public EmployeeController(EmployeeRepository studentRepository, RoleRepository roleRepository, IClassesService classesService, PasswordEncoder encoder, ManyEmployeesRepository manyStudentsRepository) {
        this.studentRepository = studentRepository;
        this.roleRepository = roleRepository;
        this.classesService = classesService;
        this.encoder = encoder;
        this.manyStudentsRepository = manyStudentsRepository;
    }

    @PostMapping("/registerStudent")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (studentRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (studentRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Employee student = new Employee(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getPatronymic(),
                encoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<String> strClasses = signUpRequest.getClasses();
        Set<Classes> classes = new HashSet<>();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new BadRoleException("Error: Role not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new BadRoleException("Error: Role not found."));
                roles.add(userRole);
            });
        }
        assert strClasses != null;
        strClasses.forEach(c -> {
            Classes classes1 = classesService.findById(c).orElseThrow(() -> new ClassNotFoundException("Error: Class not found"));
            classes.add(classes1);
        });


        student.setRoles(roles);
        student.setClasses(classes);
        studentRepository.save(student);
        return ResponseEntity.ok(new MessageResponse("Student registered successfully!"));
    }

    @PostMapping("/registerManyStudents")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> registerManyStudents(@Valid @RequestBody RegisterManyEmployeeRequest signUpRequest) {

        ListOfManyEmployees list = new ListOfManyEmployees(signUpRequest.getStudents());

        manyStudentsRepository.save(list);
        return ResponseEntity.ok(new MessageResponse("Students successful registered"));
    }

    @PutMapping("/updateStudent/{id}")
    public Employee updateStudent(@PathVariable String id, @RequestBody Employee user) {

        Optional<Employee> byId = studentRepository.findById(id);
        if (byId.isPresent()) {
            Employee student = byId.get();

            if (student.getUsername().equals(user.getUsername())) {
                student.setUsername(user.getUsername());
            } else if (studentRepository.existsByUsername(user.getUsername())) {
                throw new RuntimeException("Error: username is already exist!");
            }
            if (student.getEmail().equals(user.getEmail())) {
                student.setEmail(user.getEmail());
            } else if (studentRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("Error: email is already exist!");
            }

            student.setUsername(user.getUsername());
            student.setFirstName(user.getFirstName());
            student.setLastName(user.getLastName());
            student.setPatronymic(user.getPatronymic());
            student.setEmail(user.getEmail());
            student.setRoles(user.getRoles());

            Set<String> strClass = user.getClasses2();
            Set<Classes> classes = new HashSet<>();

            assert strClass != null;
            strClass.forEach(x -> {
                Classes classes1 = classesService.findById(x).orElseThrow(() -> new ClassNotFoundException("Error: Class not found"));
                classes.add(classes1);
            });

            student.setClasses(classes);
            return studentRepository.save(student);
        } else {
            throw new UserNotFoundException("user not found");
        }
    }

}
