package mongodb.controllers;

import mongodb.exception.BadRoleException;
import mongodb.exception.ClassNotFoundException;
import mongodb.exception.SubjNotFoundException;
import mongodb.exception.UserNotFoundException;
import mongodb.models.*;
import mongodb.payload.request.LoginRequest;
import mongodb.payload.request.SignUpRequest;
import mongodb.payload.response.JwtResponse;
import mongodb.payload.response.MessageResponse;
import mongodb.repository.RoleRepository;
import mongodb.repository.SubjRepository;
import mongodb.repository.UserRepository;
import mongodb.security.jwt.JwtUtils;
import mongodb.services.IClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubjRepository subjService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final IClassesService classesService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, SubjRepository subjService, PasswordEncoder encoder, JwtUtils jwtUtils, IClassesService classesService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.subjService = subjService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.classesService = classesService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, roles));
    }

    @PostMapping("/registerTeacher")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getPatronymic(),
                encoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<String> strSubjects = signUpRequest.getSubjects();
        Set<Subject> subjects = new HashSet<>();
        Set<Role> roles = new HashSet<>();
        Set<String> strClasses = signUpRequest.getClasses();
        Set<Classes> classes = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                    .orElseThrow(() -> new BadRoleException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("ROLE_MODERATOR".equals(role)) {
                    Role moderRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new BadRoleException("Error: Role is not found."));
                    roles.add(moderRole);
                }
            });
        }
        assert strSubjects != null;
        strSubjects.forEach(x -> {
            Subject classes1 = subjService.findById(x).orElseThrow(() -> new SubjNotFoundException("Error: Subj not found"));
            subjects.add(classes1);
        });
        assert strClasses != null;
        strClasses.forEach(c -> {
            Classes classes1 = classesService.findById(c).orElseThrow(() -> new ClassNotFoundException("Error: Class not found"));
            classes.add(classes1);
        });

        user.setClasses(classes);
        user.setRoles(roles);
        user.setSubject(subjects);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Teacher registered successfully!"));
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
        return id;
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {

        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User updatedUser = byId.get();

            if (updatedUser.getUsername().equals(user.getUsername())) {
                updatedUser.setUsername(user.getUsername());
            } else if (userRepository.existsByUsername(user.getUsername())) {
                throw new RuntimeException("Error: username is already exist!");
            }

            if (updatedUser.getEmail().equals(user.getEmail())) {
                updatedUser.setEmail(user.getEmail());
            } else if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("Error: email is already exist!");
            }

            updatedUser.setUsername(user.getUsername());
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setPatronymic(user.getPatronymic());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setRoles(user.getRoles());

            Set<String> strSubjects = user.getSubjects();
            Set<Subject> subjects = new HashSet<>();
            Set<String> strClasses = user.getClasses1();
            Set<Classes> classes = new HashSet<>();

            assert strSubjects != null;
            strSubjects.forEach(x -> {
                Subject subject = subjService.findById(x).orElseThrow(() -> new SubjNotFoundException("Error: Subj not found"));
                subjects.add(subject);
            });

            assert strClasses != null;
            strClasses.forEach(c -> {
                Classes classes1 = classesService.findById(c).orElseThrow(() -> new ClassNotFoundException("Error: Class not found"));
                classes.add(classes1);
            });

            updatedUser.setClasses(classes);
            updatedUser.setSubject(subjects);
            return userRepository.save(updatedUser);
        } else {
            throw new UserNotFoundException("user not found");
        }
    }

    @PutMapping("/updatePass/{id}")
    @Transactional
    public User updatePass(@PathVariable String id, @RequestBody User user) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user1 = byId.get();
            String p = encoder.encode(user.getPassword());
            user1.setPassword(p);
            return userRepository.save(user1);
        } else {
            throw new UserNotFoundException("Error: User not found");
        }
    }

    @GetMapping("/userRoles/{role}")
    public List<User> findByRole(@PathVariable String role) {
        return userRepository.findByRoles(role);
    }

    @GetMapping("/userName/{username}")
    public Optional<User> findByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    @GetMapping("/userByToken")
    public ResponseEntity<User> getUserByToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }

}