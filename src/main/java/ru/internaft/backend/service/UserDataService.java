package ru.f.backend.service;

import org.springframework.stereotype.Component;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.UsersDataRepository;

import java.util.Optional;

@Component
public class UserDataService {
    private final UsersDataRepository usersDataRepository;

    public UserDataService(UsersDataRepository usersDataRepository) {
        this.usersDataRepository = usersDataRepository;
    }

    public UserData findByEmail(String string){
       return usersDataRepository.findByEmail(string);
    }
}
