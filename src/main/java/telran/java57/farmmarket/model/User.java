package telran.java57.farmmarket.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    private Set<Role> roles = new HashSet<>();
}
