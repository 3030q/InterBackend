package ru.internaft.backend.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.internaft.backend.repository.LoginsDataRepository;

@RestController //только для тестов
public class UtilityController {

    private final LoginsDataRepository loginsDataRepository;

    public UtilityController(LoginsDataRepository loginsDataRepository) {
        this.loginsDataRepository = loginsDataRepository;
    }

    public Integer getCurrentUserId() {
        String loginCurrentUser = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return loginsDataRepository.findByLogin(loginCurrentUser).getId();
    }

    @GetMapping("/h")
    public Integer h(){
        return getCurrentUserId();
    }
}
