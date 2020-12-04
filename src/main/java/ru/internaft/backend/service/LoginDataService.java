package ru.internaft.backend.service;

import org.springframework.stereotype.Service;
import ru.internaft.backend.entity.LoginData;
import ru.internaft.backend.repository.LoginsDataRepository;

import java.util.Optional;

@Service
public class LoginDataService {
    private final LoginsDataRepository loginsDataRepository;

    public LoginDataService(LoginsDataRepository loginsDataRepository) {
        this.loginsDataRepository = loginsDataRepository;
    }

    public LoginData findByLogin(String string) {
        return loginsDataRepository.findByLogin(string);
    }

    public Optional<LoginData> findById(String id) {
        return loginsDataRepository.findById(id);
    }
}
