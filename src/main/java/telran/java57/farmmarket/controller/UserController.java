package telran.java57.farmmarket.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import telran.java57.farmmarket.dto.RolesDto;
import telran.java57.farmmarket.dto.UpdateUserDto;
import telran.java57.farmmarket.dto.UserDto;
import telran.java57.farmmarket.dto.UserRegisterDto;
import telran.java57.farmmarket.dto.exceptions.UserNotFoundException;
import telran.java57.farmmarket.model.User;
import telran.java57.farmmarket.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto){
        return userService.register(userRegisterDto);
    }
    @DeleteMapping("/user/{login}")
    public UserDto removeUser(@PathVariable String login){
        return userService.removeUser(login);
    }
    @GetMapping("/user/{login}")
    public UserDto getUserByLogin(@PathVariable String login){
        return userService.getUser(login);
    }
    @PutMapping("/user/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(login,updateUserDto);
    }
    @PutMapping("/user/{login}/role/{role}")
    public RolesDto addRole(@PathVariable String login, @PathVariable String role){
        return userService.changeRolesList(login,role,true);
    }
    @DeleteMapping("/user/{login}/role/{role}")
    public RolesDto removeRole(@PathVariable String login, @PathVariable String role){
        return userService.changeRolesList(login,role,false);
    }
    @PostMapping("/login")
    public UserDto login(Principal principal){
        return userService.getUser(principal.getName());
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(Principal principal,@RequestHeader("X-Password") String newPassword){
        userService.changePassword(principal.getName(),newPassword);
    }
    @GetMapping("/users")
    public List<UserDto> getAllUsers(Principal principal) {
        return userService.getAllUsers();
    }
}
