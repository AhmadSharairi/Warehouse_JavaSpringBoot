package com.springjwt.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springjwt.dto.AuthenticationDTO;
import com.springjwt.dto.AuthenticationResponse;
import com.springjwt.repositories.RoleRepository;
import com.springjwt.services.user.IUserService;
import com.springjwt.util.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping()
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;



    @Autowired
    private IUserService userService;

    @PostMapping("/authenticate")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationDTO authenticationDTO, 
                                                            HttpServletResponse response) throws IOException 
    {
        
        var user = userService.getUserByUsername(authenticationDTO.getUsername());
    
        try {
            // Authenticate the user using the AuthenticationManager
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                    authenticationDTO.getPassword()));
        } catch (BadCredentialsException e) {
            logger.error("Incorrect username or password!", e);
            throw new BadCredentialsException("Incorrect username or password!");
        } catch (DisabledException disabledException) {
            logger.error("User is not activated", disabledException);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not activated");
            return null;
        } catch (Exception ex) {
            logger.error("Authentication error", ex);
            throw new RuntimeException("Authentication failed");
        }
    
    
        final String jwt = jwtUtil.generateToken(user);
    
        return new AuthenticationResponse(jwt);
    }
    
}
