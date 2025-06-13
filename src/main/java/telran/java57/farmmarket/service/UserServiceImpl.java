package telran.java57.farmmarket.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.dao.UserRepository;
import telran.java57.farmmarket.dto.RolesDto;
import telran.java57.farmmarket.dto.UpdateUserDto;
import telran.java57.farmmarket.dto.UserDto;
import telran.java57.farmmarket.dto.UserRegisterDto;
import telran.java57.farmmarket.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
     final UserRepository userRepository;
     final ModelMapper modelMapper;


    @Override
    public UserDto registerNewUser(UserRegisterDto dto) {
        return null;
    }

    @Override
    public UserDto getUserByLogin(String login) {
        return null;
    }

    @Override
    public void changePassword(String login, String newPassword) {

    }

    @Override
    public UserDto removeUser(String login) {
        return null;
    }

    @Override
    public UserDto updateUser(String login, UpdateUserDto updateUserDto) {
        return null;
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        return null;
    }
}
