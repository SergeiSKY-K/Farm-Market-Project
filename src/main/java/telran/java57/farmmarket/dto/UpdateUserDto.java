package telran.java57.farmmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    String firstName;
    String lastName;
    private Set<String> roles;
}
