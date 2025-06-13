package telran.java57.farmmarket.dto;

import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolesDto {
    String login;
    @Singular
    Set<String> roles;
}
