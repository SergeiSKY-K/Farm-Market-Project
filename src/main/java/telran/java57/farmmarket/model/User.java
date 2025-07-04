package telran.java57.farmmarket.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Getter
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String login;
    @Setter
    private String password;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Singular
    @Setter
    private Set<Role> roles = new HashSet<>();

    public User(String login, String password, String firstName, String lastName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
        roles = new HashSet<>();
        roles.add(Role.USER);
    }

    public boolean addRole(String role){
        return roles.add(Role.valueOf(role.toUpperCase()));
    }
    public boolean removeRole(String role){
        return roles.remove(Role.valueOf(role.toUpperCase()));
    }
}

