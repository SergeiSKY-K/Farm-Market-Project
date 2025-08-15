package telran.java57.farmmarket.controller;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import telran.java57.farmmarket.dao.RefreshTokenRepository;
import telran.java57.farmmarket.dao.UserRepository;
import telran.java57.farmmarket.dto.*;
import telran.java57.farmmarket.security.JwtUtil;
import telran.java57.farmmarket.security.UserDetailsServiceImpl;
import telran.java57.farmmarket.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }

    @DeleteMapping("/user/{login}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or #login == authentication.name")
    public UserDto removeUser(@PathVariable String login) {
        return userService.removeUser(login);
    }

    @GetMapping("/user/{login}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or #login == authentication.name")
    public UserDto getUserByLogin(@PathVariable String login) {
        return userService.getUser(login);
    }

    @PutMapping("/user/{login}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or #login == authentication.name")
    public UserDto updateUser(@PathVariable String login, @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(login, updateUserDto);
    }

    @PutMapping("/user/{login}/role/{role}")
    public RolesDto addRole(@PathVariable String login, @PathVariable String role) {
        return userService.changeRolesList(login, role, true);
    }

    @DeleteMapping("/user/{login}/role/{role}")
    public RolesDto removeRole(@PathVariable String login, @PathVariable String role) {
        return userService.changeRolesList(login, role, false);
    }

//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(Authentication authentication, HttpServletResponse response) {
//        String login = authentication.getName();
//
//        refreshTokenRepository.deleteById(login);
//
//
//        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
//                .httpOnly(true)
//                .path("/")
//                .secure(true)
//                .sameSite("Strict")
//                .maxAge(0)
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//
//        return ResponseEntity.noContent().build();
//    }


    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(Authentication authentication, @RequestBody ChangePasswordDto body) {
        userService.changePassword(authentication, body);
    }
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/suppliers")
    public List<UserDto> getAllSuppliers() {
        return userService.getAllSuppliers();
    }
}