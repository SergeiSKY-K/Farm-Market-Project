package telran.java57.farmmarket.service;

import telran.java57.farmmarket.dto.RolesDto;
import telran.java57.farmmarket.dto.UpdateUserDto;
import telran.java57.farmmarket.dto.UserDto;
import telran.java57.farmmarket.dto.UserRegisterDto;

import java.util.List;

public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto);
    UserDto getUser(String login);
    void changePassword(String login,String newPassword);
    UserDto removeUser(String login);
    UserDto updateUser(String login, UpdateUserDto updateUserDto);
    RolesDto changeRolesList(String login, String role, boolean isAddRole);
    List<UserDto> getAllUsers();
}
