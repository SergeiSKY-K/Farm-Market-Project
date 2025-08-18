package telran.java57.farmmarket.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import telran.java57.farmmarket.model.Role;


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
    http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authorizeHttpRequests(auth -> auth
            // public
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/users/register").permitAll()
            .requestMatchers(HttpMethod.GET, "/product").permitAll()
            .requestMatchers(HttpMethod.GET, "/product/{id}").permitAll()
            .requestMatchers(HttpMethod.GET, "/product/category/{category}").permitAll()

            // self-service
            .requestMatchers(HttpMethod.GET, "/users/user/{login}").authenticated()
            .requestMatchers(HttpMethod.PUT, "/users/user/{login}").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/users/user/{login}").authenticated()
            .requestMatchers(HttpMethod.PUT, "/users/password").authenticated()

            // admin / moderator
            .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMINISTRATOR")
            .requestMatchers(HttpMethod.GET, "/users/suppliers").hasAnyRole("MODERATOR","ADMINISTRATOR")
            .requestMatchers(HttpMethod.PUT, "/users/user/{login}/role/SUPPLIER").hasAnyRole("MODERATOR","ADMINISTRATOR")
            .requestMatchers(HttpMethod.DELETE, "/users/user/{login}/role/SUPPLIER").hasAnyRole("MODERATOR","ADMINISTRATOR")
            .requestMatchers(HttpMethod.PUT, "/users/user/{login}/role/{role}").hasRole("ADMINISTRATOR")
            .requestMatchers(HttpMethod.DELETE, "/users/user/{login}/role/{role}").hasRole("ADMINISTRATOR")

            // product
            .requestMatchers(HttpMethod.POST, "/product").hasAnyRole("ADMINISTRATOR","SUPPLIER")
            .requestMatchers(HttpMethod.PUT, "/product/{id}").hasAnyRole("ADMINISTRATOR","SUPPLIER")
            .requestMatchers(HttpMethod.DELETE, "/product/{id}").hasAnyRole("ADMINISTRATOR","SUPPLIER")
            .requestMatchers(HttpMethod.PUT, "/product/{id}/status").hasAnyRole("MODERATOR","ADMINISTRATOR")
            .requestMatchers(HttpMethod.GET, "/product/my-products").hasAnyRole("SUPPLIER","ADMINISTRATOR")
            .requestMatchers(HttpMethod.GET, "/product/blocked").hasAnyRole("MODERATOR","ADMINISTRATOR")

            // orders
            .requestMatchers(HttpMethod.POST, "/orders").authenticated()
            .requestMatchers(HttpMethod.POST, "/orders/*/pay").authenticated()
            .requestMatchers(HttpMethod.GET,  "/orders/my").authenticated()
            .requestMatchers(HttpMethod.GET, "/orders/supplier").hasAnyRole("SUPPLIER","ADMINISTRATOR")
            .requestMatchers(HttpMethod.GET, "/orders/moderator").hasRole("MODERATOR")

            // upload
            .requestMatchers(HttpMethod.POST, "/files").hasAnyRole("ADMINISTRATOR","SUPPLIER")

            .anyRequest().authenticated()
    );

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
//    //@Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
}
