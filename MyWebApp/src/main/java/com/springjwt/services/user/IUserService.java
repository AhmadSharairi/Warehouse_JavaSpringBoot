package com.springjwt.services.user;



import java.util.List;
import java.util.Optional;

import com.springjwt.entities.User;

public interface IUserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User getUserByUsername(String username);

    
}
