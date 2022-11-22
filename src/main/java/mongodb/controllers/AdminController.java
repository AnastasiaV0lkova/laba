package com.alchotest.spring.jwt.mongodb.controllers;

import com.alchotest.spring.jwt.mongodb.exception.BadRoleException;
import com.alchotest.spring.jwt.mongodb.models.Admin;
import com.alchotest.spring.jwt.mongodb.models.ERole;
import com.alchotest.spring.jwt.mongodb.models.Role;
import com.alchotest.spring.jwt.mongodb.payload.request.SignUpRequest;
import com.alchotest.spring.jwt.mongodb.payload.response.MessageResponse;
import com.alchotest.spring.jwt.mongodb.repository.AdminRepository;
import com.alchotest.spring.jwt.mongodb.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AdminController(AdminRepository adminRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (adminRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (adminRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Admin admin = new Admin(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getPatronymic(),
                encoder.encode(signUpRequest.getPassword())
        );
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new BadRoleException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("ROLE_ADMIN".equals(role)) {
                    Role moderRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new BadRoleException("Error: Role is not found."));
                    roles.add(moderRole);
                }
            });
        }

        admin.setRoles(roles);
        adminRepository.save(admin);

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }

    @PutMapping("/updateAdmin/{id}")
    public Admin updateAdmin(@PathVariable String id, @RequestBody Admin admin) {

        Optional<Admin> byId = adminRepository.findById(id);
        if (byId.isPresent()) {
            Admin admin1 = byId.get();

            if (admin1.getUsername().equals(admin.getUsername())){
                admin1.setUsername(admin.getUsername());
            }
            else if (adminRepository.existsByUsername(admin.getUsername())) {
                throw new RuntimeException("Error: username is already exist!");
            }
            if (admin1.getEmail().equals(admin.getEmail())){
                admin1.setEmail(admin.getEmail());
            }
            else if (adminRepository.existsByEmail(admin.getEmail())) {
                throw new RuntimeException("Error: email is already exist!");
            }

            admin1.setUsername(admin.getUsername());
            admin1.setFirstName(admin.getFirstName());
            admin1.setLastName(admin.getLastName());
            admin1.setPatronymic(admin.getPatronymic());
            admin1.setEmail(admin.getEmail());
            admin1.setRoles(admin.getRoles());

            return adminRepository.save(admin1);
        } else {
            throw new RuntimeException("admin not found");
        }
    }
}
