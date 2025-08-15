package telran.java57.farmmarket.service;

import org.springframework.security.core.Authentication;
import telran.java57.farmmarket.dto.*;

import java.util.List;

public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto);
    UserDto getUser(String login);
    void changePassword(Authentication authentication, ChangePasswordDto dto);
    UserDto removeUser(String login);
    UserDto updateUser(String login, UpdateUserDto updateUserDto);
    RolesDto changeRolesList(String login, String role, boolean isAddRole);
    List<UserDto> getAllUsers();
    List<UserDto> getAllSuppliers();
}
