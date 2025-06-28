package telran.java57.farmmarket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/users/register").permitAll()
                .requestMatchers(HttpMethod.DELETE,"users/user/{login}")
                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                .requestMatchers(HttpMethod.PUT, "/users/password").authenticated()
                .requestMatchers(HttpMethod.PUT, "/users/user/{login}/role/{role}").hasRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.DELETE, "/users/user/{login}/role/{role}").hasRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.GET, "/users/user/{login}")
                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                .requestMatchers(HttpMethod.PUT, "/users/user/{login}")
                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                .requestMatchers(HttpMethod.GET, "/users/users").hasRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.GET, "/product").permitAll()
                .requestMatchers(HttpMethod.GET, "/product/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/product/category/{category}").permitAll()
                .requestMatchers(HttpMethod.POST, "/product").hasRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.PUT, "/product/{id}").hasRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.DELETE, "/product/{id}").hasRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.POST, "/orders").authenticated()
                .requestMatchers(HttpMethod.POST, "/product").hasRole("ADMINISTRATOR")
                .anyRequest().authenticated()
        );

        return http.build();
    }
}
