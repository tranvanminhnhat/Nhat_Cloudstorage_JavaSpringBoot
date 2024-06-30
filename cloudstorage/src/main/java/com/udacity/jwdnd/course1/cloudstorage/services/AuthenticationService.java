package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {

    // Declare variable
    private HashService hashService;
    private UserMapper userMapper;

    public AuthenticationService(UserMapper userMapper, HashService hashService){
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Get username
        String username = authentication.getName();
        // Get password
        String password = authentication.getCredentials().toString();

        // Get User by username
        User user = userMapper.getUser(username);

        if(user != null){
            String encodeSalt = user.getSalt();
            // Get hashedValue of password
            String hashedPassword = hashService.getHashedValue(password, encodeSalt);

            // Check correct password
            if (user.getPassword().equals(hashedPassword)){
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }
        }

        // If User not exist then return null
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
