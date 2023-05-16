package com.example.security.services;

import com.example.security.objects.User;
import com.example.security.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> getUsersByParams(Map<String, Object> params) {
        UUID id = null;
        String firstname = (String) params.get("firstname");
        String lastname = (String) params.get("lastname");
        String email = (String) params.get("email");
        String username = (String) params.get("username");
        if (params.containsKey("id") && (!params.get("id").equals(""))) {
                id = UUID.fromString((String) params.get("id"));

        }

        return usersRepository.findUsersByParams(id, firstname, lastname, email, username);
    }

}