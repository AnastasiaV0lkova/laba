package mongodb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    private String id;

    @NotBlank
    @Size(min = 4, max = 60)
    private String username;

    @NotBlank
    @Size(min = 4, max = 60)
    @Email
    private String email;

    @NotBlank
    @Size(max = 60)
    private String firstName, lastName, patronymic;

    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    @DBRef
    private Set<Subject> subject = new HashSet<>();
    private Set<String> subjects;

    @DBRef
    private Set<Classes> classes = new HashSet<>();
    private Set<String> classes1;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String firstName, String lastName, String patronymic, String password) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
