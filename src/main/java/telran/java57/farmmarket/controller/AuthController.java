package telran.java57.farmmarket.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import telran.java57.farmmarket.dao.RefreshTokenRepository;
import telran.java57.farmmarket.dao.UserRepository;
import telran.java57.farmmarket.dto.LoginDto;
import telran.java57.farmmarket.dto.UserDto;
import telran.java57.farmmarket.security.AuthService;
import telran.java57.farmmarket.security.JwtUtil;
import telran.java57.farmmarket.security.UserDetailsServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        return authService.login(loginDto, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication, HttpServletResponse response) {
        return authService.logout(authentication.getName(), response);
    }
}