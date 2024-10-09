package com.springjwt.services.auth;

import com.springjwt.dto.SignupDto;
import com.springjwt.dto.UserDto;
import com.springjwt.entities.User;
import com.springjwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(SignupDto signupDTO) {
        User user = new User();
        user.setFullName(signupDTO.getFullname());
        user.setUsername(signupDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
        User createdUser = userRepository.save(user);
        UserDto userDTO = new UserDto();
        userDTO.setId(createdUser.getId());
        userDTO.setUsername(createdUser.getUsername());
        userDTO.setFullname(createdUser.getFullName());
        return userDTO;
    }
}
