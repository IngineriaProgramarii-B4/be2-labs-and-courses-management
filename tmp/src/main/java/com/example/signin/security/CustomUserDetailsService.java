package com.example.signin.security;

import com.example.security.repositories.UsersRepository;
import com.example.signin.model.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.example.security.objects.User user = usersRepository.findByEmail(email);
        if(user == null)
            throw new UsernameNotFoundException("Username not found");
        return new User(user.getEmail(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
    }
    private List<SimpleGrantedAuthority> mapRolesToAuthorities(List<Role> roles)
    {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }
}
