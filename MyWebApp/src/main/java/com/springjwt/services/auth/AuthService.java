package com.springjwt.services.auth;

import com.springjwt.dto.SignupDto;
import com.springjwt.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignupDto signupDTO);
}
