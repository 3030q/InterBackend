package ru.internaft.backend.service;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.internaft.backend.entity.LoginData;
import ru.internaft.backend.repository.LoginsDataRepository;

import java.util.Collections;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final LoginsDataRepository loginsDataRepository;

    public UserDetailsServiceImpl(LoginsDataRepository loginsDataRepository) {
        this.loginsDataRepository = loginsDataRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        LoginData user = loginsDataRepository.findById(login).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(login);
        }
        return new User(user.getLogin(), user.getPassword(), Collections.emptyList());
    }
}

