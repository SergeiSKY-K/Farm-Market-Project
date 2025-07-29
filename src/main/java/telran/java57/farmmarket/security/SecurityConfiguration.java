package telran.java57.farmmarket.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/users/register").permitAll()
//                .requestMatchers(HttpMethod.DELETE,"users/user/{login}")
//                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                        .requestMatchers(HttpMethod.DELETE, "/users/user/{login}").authenticated()
                .requestMatchers(HttpMethod.PUT, "/users/password").authenticated()
                .requestMatchers(HttpMethod.PUT, "/users/user/{login}/role/SUPPLIER")
                .hasAnyRole("MODERATOR", "ADMINISTRATOR")
                .requestMatchers(HttpMethod.PUT, "/users/user/{login}/role/{role}")
                .hasRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.DELETE, "/users/user/{login}/role/SUPPLIER")
                .hasAnyRole("MODERATOR", "ADMINISTRATOR")
                .requestMatchers(HttpMethod.DELETE, "/users/user/{login}/role/{role}")
                .hasRole("ADMINISTRATOR")
//                .requestMatchers(HttpMethod.GET, "/users/user/{login}")
//                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                        .requestMatchers(HttpMethod.GET, "/users/user/{login}").authenticated()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/refresh").permitAll()
                .requestMatchers("/auth/logout").permitAll()
//                .requestMatchers(HttpMethod.PUT, "/users/user/{login}")
//                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                        .requestMatchers(HttpMethod.PUT, "/users/user/{login}").authenticated()
                .requestMatchers(HttpMethod.GET, "/users/users").hasRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.GET, "/product").permitAll()
                .requestMatchers(HttpMethod.GET, "/product/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/product/category/{category}").permitAll()

                .requestMatchers(HttpMethod.POST, "/product").hasAnyRole("ADMINISTRATOR","SUPPLIER")
                .requestMatchers(HttpMethod.PUT, "/product/{id}").hasAnyRole("ADMINISTRATOR","SUPPLIER")
                .requestMatchers(HttpMethod.DELETE, "/product/{id}").hasAnyRole("ADMINISTRATOR","SUPPLIER")

                .requestMatchers(HttpMethod.POST, "/orders").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/orders/{id}/pay").hasRole("USER")

                .requestMatchers(HttpMethod.GET, "/orders/supplier").hasRole("SUPPLIER")
                .requestMatchers(HttpMethod.GET, "/users/suppliers").hasAnyRole("MODERATOR", "ADMINISTRATOR")
                .requestMatchers(HttpMethod.GET, "/orders/moderator").hasRole("MODERATOR")
                .requestMatchers(HttpMethod.PUT, "/product/{id}/status").hasAnyRole("MODERATOR", "ADMINISTRATOR")
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
