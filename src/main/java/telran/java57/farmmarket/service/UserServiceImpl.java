package telran.java57.farmmarket.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.dao.UserRepository;
import telran.java57.farmmarket.dto.RolesDto;
import telran.java57.farmmarket.dto.UpdateUserDto;
import telran.java57.farmmarket.dto.UserDto;
import telran.java57.farmmarket.dto.UserRegisterDto;
import telran.java57.farmmarket.dto.exceptions.UserExistsException;
import telran.java57.farmmarket.dto.exceptions.UserNotFoundException;
import telran.java57.farmmarket.model.Role;
import telran.java57.farmmarket.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
     final UserRepository userRepository;
     final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRegisterDto dto) {
        if(userRepository.existsById(dto.getLogin())){
            throw new UserExistsException();
        }
        User user = modelMapper.map(dto,User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.getRoles().add(Role.USER);
        userRepository.save(user);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        User user = userRepository.findById(login).orElseThrow(()->new UserNotFoundException(login));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {
        User user = userRepository.findById(login).orElseThrow(()->new UserNotFoundException(login));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserDto removeUser(String login) {
        User user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        userRepository.delete(user);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        if (updateUserDto.getFirstName()!=null){
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName()!=null){
            user.setLastName(updateUserDto.getLastName());
        }
        if (updateUserDto.getRoles() != null && !updateUserDto.getRoles().isEmpty()) {
            Set<Role> newRoles = updateUserDto.getRoles().stream()
                    .map(String::toUpperCase)
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            user.setRoles(newRoles);
        }
        userRepository.save(user);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        User user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        boolean res;
        if (isAddRole) {
            res = user.addRole(role);
        } else {
            res = user.removeRole(role);
        }
        if (res) {
            userRepository.save(user);
        }
        return modelMapper.map(user, RolesDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}
